package com.sequenceiq.cloudbreak.template.filesystem;

import java.util.Optional;

public class FileSystemConfigQueryObject {

    private final String clusterName;

    private final String storageName;

    private final String blueprintText;

    private final String fileSystemType;

    private final Optional<String> accountName;

    private final boolean attachedCluster;

    private final boolean secure;

    private FileSystemConfigQueryObject(Builder builder) {
        storageName = builder.storageName;
        clusterName = builder.clusterName;
        blueprintText = builder.blueprintText;
        fileSystemType = builder.fileSystemType;
        accountName = builder.accountName;
        attachedCluster = builder.attachedCluster;
        secure = builder.secure;
    }

    public String getClusterName() {
        return clusterName;
    }

    public String getStorageName() {
        return storageName;
    }

    public String getBlueprintText() {
        return blueprintText;
    }

    public String getFileSystemType() {
        return fileSystemType;
    }

    public Optional<String> getAccountName() {
        return accountName;
    }

    public boolean isAttachedCluster() {
        return attachedCluster;
    }

    public boolean isSecure() {
        return secure;
    }

    public static class Builder {

        private String clusterName;

        private String storageName;

        private String blueprintText;

        private String fileSystemType;

        private Optional<String> accountName = Optional.empty();

        private boolean attachedCluster;

        private boolean secure;

        public static Builder builder() {
            return new Builder();
        }

        public Builder withStorageName(String storageName) {
            this.storageName = storageName;
            return this;
        }

        public Builder withClusterName(String clusterName) {
            this.clusterName = clusterName;
            return this;
        }

        public Builder withBlueprintText(String blueprintText) {
            this.blueprintText = blueprintText;
            return this;
        }

        public Builder withFileSystemType(String fileSystemType) {
            this.fileSystemType = fileSystemType;
            return this;
        }

        public Builder withAccountName(String accountName) {
            this.accountName = Optional.ofNullable(accountName);
            return this;
        }

        public Builder withAttachedCluster(boolean attachedCluster) {
            this.attachedCluster = attachedCluster;
            return this;
        }

        public Builder withSecure(boolean secure) {
            this.secure = secure;
            return this;
        }

        public FileSystemConfigQueryObject build() {
            return new FileSystemConfigQueryObject(this);
        }
    }
}
