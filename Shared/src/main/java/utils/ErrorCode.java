package utils;

/**
 * Every error that needs to be notified to the client needs to be permitted here and needs to implement this interface
 */
public sealed interface ErrorCode permits AuthError { }
