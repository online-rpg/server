package com.vrpg.server.management;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN, reason = "Wrong file type!")
class InvalidFileException extends RuntimeException {
}
