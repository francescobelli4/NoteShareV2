package app.views.fx;

import app.views.Navigator;

public class FxNavigator extends Navigator<FxView> {

    private final FxFrameManager frameManager;

    public FxNavigator(FxFrameManager frameManager) {
        this.frameManager = frameManager;
    }

    @Override
    protected void display(FxView view) {
        frameManager.show(view);
    }
}
