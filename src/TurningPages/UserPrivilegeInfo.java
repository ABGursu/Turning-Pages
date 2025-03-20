package TurningPages;

public class UserPrivilegeInfo {
    private String username;
    private boolean readOnly;

    public UserPrivilegeInfo(String username, boolean readOnly) {
        this.username = username;
        this.readOnly = readOnly;
    }

    public String getUsername() {
        return username;
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    @Override
    public String toString() {
        return username + (readOnly ? " (Read-Only)" : " (Full Access)");
    }
} 