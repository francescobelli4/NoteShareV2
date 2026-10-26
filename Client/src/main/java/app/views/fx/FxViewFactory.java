package app.views.fx;

import app.models.Models;
import app.views.fx.graphiccontrollers.FxHomeViewController;
import app.views.fx.pages.FxHomeView;

/**
 * This class should be the factory that creates every FX view. In this way, editing a view only affects this class.
 */
public class FxViewFactory {

    private final Models models;

    public FxViewFactory(Models models) {
        this.models = models;
    }

    public FxHomeView createFxHomeView() {
        FxHomeViewController graphicController = new FxHomeViewController();
        return new FxHomeView(graphicController, models.userModel());
    }
}
