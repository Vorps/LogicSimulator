package me.graphics.info;

/**
 * Utlisé pour programmer des actions entre les JPanels
 * @param <T>
 */
@FunctionalInterface
public interface Action<T> {

    void action(T value);
}
