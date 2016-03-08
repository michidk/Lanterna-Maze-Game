package lohr.michael.labyrinth.manager;

import lohr.michael.labyrinth.IActionListener;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Michael Lohr on 13.01.2016.
 */
public class ActionManager implements IManager, IActionListener {

    @Getter
    private static final ActionManager instance = new ActionManager();

    private List<IActionListener> listeners = new ArrayList<>();

    private ActionManager() { }

    @Override
    public void init() {

    }

    public void registerListener(IActionListener listener) {
        if (!listeners.contains(listener))
            listeners.add(listener);
    }

    public void unregisterListener(IActionListener listener) {
        if (listeners.contains(listener))
            listeners.remove(listener);
    }

    // call e.g. onUp on all listeners
    @Override
    public void onUp() {
        listeners.forEach(IActionListener::onUp);
    }

    @Override
    public void onRight() {
        listeners.forEach(IActionListener::onRight);
    }

    @Override
    public void onDown() {
        listeners.forEach(IActionListener::onDown);
    }

    @Override
    public void onLeft() {
        listeners.forEach(IActionListener::onLeft);
    }

}
