package comp90018.project2.weather.data;

import org.json.JSONObject;

/**
 * Created by sunxiaofeng208 on 2017/9/12.
 */

public class Item implements JSONPopulator {
    private Condition condition;

    public Condition getCondition() {
        return condition;
    }

    public void populate(JSONObject data) {
        condition = new Condition();
        condition.populate(data.optJSONObject("condition"));
    }
}
