package com.ashomok.eNumbers.ocr;

/**
 * Created by Iuliia on 11.11.2015.
 */
class FolderNotCreatedException extends Exception {

    public FolderNotCreatedException() {
        // TODO Auto-generated constructor stub
    }

    public FolderNotCreatedException(String detailMessage) {
        super(detailMessage);
    }

    @Override
    public String getMessage() {
        return "Folder was not created.";
    }
}
