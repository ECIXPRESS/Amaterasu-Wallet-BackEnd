package edu.dosw.AmaterasuWalletBackEnd.AmaterasuWalletBack.Application;
/**
 * Interfaz base para todos los casos de uso
 * @param <I> Tipo de entrada (Input)
 * @param <O> Tipo de salida (Output)
 */
public interface UseCase<I, O> {
    O execute(I input);
}
