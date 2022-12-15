package com.walletmgr.api.Utils;

public class UserException extends RuntimeException {

    public UserException(final String msg) {
        super("userException : " + msg);
    }
}
