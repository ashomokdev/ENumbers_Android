package com.ashomok.eNumbers.ocr;

/**
 * Created by Iuliia on 11.11.2015.
 */
class DirectoryNotCreatedException extends Exception {

    public DirectoryNotCreatedException(String detailMessage) {
        super(detailMessage);
    }

    @Override
    public String getMessage() {
        return "Folder was not created.";
    }
}
