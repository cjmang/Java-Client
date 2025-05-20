package dev.koifysh.archipelago;

public interface APEventManager {

    void post(Object event);
    void register(Object sub);
    void unregister(Object sub);
}
