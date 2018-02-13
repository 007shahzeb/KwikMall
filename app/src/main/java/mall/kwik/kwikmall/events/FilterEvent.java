package mall.kwik.kwikmall.events;

/**
 * Created by dharamveer on 1/2/18.
 */

public class FilterEvent {

    public String getFilteredIds() {
        return filteredIds;
    }

    private String filteredIds;

    public FilterEvent(String data) {
        this.filteredIds=data;
    }
}
