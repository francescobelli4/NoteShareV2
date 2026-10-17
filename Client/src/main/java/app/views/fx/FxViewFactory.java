package app.views.fx;

import app.views.fx.pages.FxHomeView;

public class FxViewFactory {

    public FxViewFactory() {
        // Nothing to do :D
    }

    public FxHomeView createFxHomeView() {
        return new FxHomeView();
    }
}
