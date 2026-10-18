package app.views.fx;

import app.views.Navigator;

/**
 * This class is a subclass of Navigator. This should implement the specific display function for the FX UI.
 */
public class FxNavigator extends Navigator<FxView<?>> {

    private final FxFrameManager frameManager;

    public FxNavigator(FxFrameManager frameManager) {
        this.frameManager = frameManager;
    }

    @Override
    protected void display(FxView<?> view) {
        frameManager.show(view);
    }
}