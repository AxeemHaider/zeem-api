package org.octabyte.zeem.API.Entity;

import java.util.List;

public class Username {

    private List<String> suggestions;
    private Boolean available = false;

    public Username() {
    }

    public List<String> getSuggestions() {
        return suggestions;
    }

    public void setSuggestions(List<String> suggestions) {
        this.suggestions = suggestions;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }
}
