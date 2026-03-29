package com.studio.core.event;

import org.springframework.context.ApplicationEvent;

public abstract class BaseEvent extends ApplicationEvent {

    private final String entidade;
    private final Long entidadeId;

    public BaseEvent(Object source, String entidade, Long entidadeId) {
        super(source);
        this.entidade = entidade;
        this.entidadeId = entidadeId;
    }

    public String getEntidade() { return entidade; }
    public Long getEntidadeId() { return entidadeId; }
}
