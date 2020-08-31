package com.looseboxes.web.chat;

import com.bc.jpa.context.PersistenceUnitContext;
import com.bc.jpa.dao.Select;
import com.looseboxes.cometd.chat.MessageConsumer;
import com.looseboxes.cometd.chat.MessageStore;
import com.looseboxes.pu.entities.Chatmessage;
import com.looseboxes.pu.entities.Chatmessage_;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

/**
 * @author Chinomso Bassey Ikwuagwu on May 19, 2018 7:53:30 PM
 */
public class MessageConsumerForDatabase implements Serializable, 
        MessageConsumer<Chatmessage>, MessageStore<Chatmessage>{

    private transient static final Logger LOG = 
            Logger.getLogger(MessageConsumerForDatabase.class.getName());

    private final PersistenceUnitContext jpaUnit;

    public MessageConsumerForDatabase(PersistenceUnitContext jpaUnit) {
        this.jpaUnit = Objects.requireNonNull(jpaUnit);
    }
    
    @Override
    public boolean accept(String from, String to, Chatmessage message) {
        Objects.requireNonNull(message);
        jpaUnit.getDao().begin().persistAndClose(message);
        LOG.finer(() -> "Persisted Chat. From: " + from + ", to: " + to + ", message: " + (message==null?null:message.getChatText()));
        return true;
    }

    @Override
    public List<Chatmessage> get(String from, String to) {
        Objects.requireNonNull(from);
        Objects.requireNonNull(to);
        try(final Select<Chatmessage> select = jpaUnit.getDaoForSelect(Chatmessage.class)) {
            return select.from(Chatmessage.class)
                    .where(Chatmessage_.fromEmail, from)
                    .and().where(Chatmessage_.toEmail, to)
                    .createQuery().getResultList();
        }
    }
}
