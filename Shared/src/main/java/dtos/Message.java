package dtos;

import java.io.Serializable;

public sealed interface Message extends Serializable permits Request, Response {
}
