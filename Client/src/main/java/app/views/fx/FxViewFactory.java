package app.views.fx;

import app.views.fx.pages.FxHomeView;

/**
 * This class should be the factory that creates every FX view. In this way, editing a view only affects this class.
 */
public class FxViewFactory {

    public FxViewFactory() {
        // Nothing to do :D
    }

    public FxHomeView createFxHomeView() {
        return new FxHomeView();
    }
}
