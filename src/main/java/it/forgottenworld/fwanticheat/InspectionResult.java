package it.forgottenworld.fwanticheat;

import java.io.Serializable;

public enum InspectionResult implements Serializable {
    IO_EXCEPTION,
    FILE_NOT_FOUND,
    HASH_FUNCTION_NOT_FOUND,
    INVALID_TEXTURE_FORMAT,
    NORMAL;
}