package ap.runescape;

import dev.koifysh.archipelago.APEventManager;

public class RSEventManager implements APEventManager {

    public interface EventBus {
        void post(Object obj);
        void register(Object obj);
        void unregister(Object obj);
    }

    private final EventBus eventBus;

    public RSEventManager(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    @Override
    public void post(Object event) {
        eventBus.post(event);
    }

    @Override
    public void register(Object sub) {
        eventBus.register(sub);
    }

    @Override
    public void unregister(Object sub) {
        eventBus.unregister(sub);
    }
}
