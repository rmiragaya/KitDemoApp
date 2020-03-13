package com.rodrigo.kitdemoapp.Models;

import java.io.File;

public class DocumentVMandFile {

    private DocumentViewModel dvm;
    private File file;

    public DocumentVMandFile(DocumentViewModel dvm, File file) {
        this.dvm = dvm;
        this.file = file;
    }

    public DocumentViewModel getDvm() {
        return dvm;
    }

    public void setDvm(DocumentViewModel dvm) {
        this.dvm = dvm;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
}
