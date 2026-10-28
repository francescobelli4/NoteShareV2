package app.views.fx;

import app.appcontrollers.AppControllers;
import app.models.Models;
import app.views.fx.graphiccontrollers.auth.FxAuthLoginViewController;
import app.views.fx.graphiccontrollers.auth.FxAuthRegisterViewController;
import app.views.fx.graphiccontrollers.auth.FxAuthViewController;
import app.views.fx.graphiccontrollers.home.FxHomeViewController;
import app.views.fx.pages.auth.FxAuthLoginView;
import app.views.fx.pages.auth.FxAuthRegisterView;
import app.views.fx.pages.auth.FxAuthView;
import app.views.fx.pages.home.FxHomeView;

/**
 * This class should be the factory that creates every FX view. In this way, editing a view only affects this class.
 */
public class FxViewFactory {

    private final FxNavigator navigator;
    private final Models models;
    private final AppControllers appControllers;

    public FxViewFactory(FxNavigator navigator, Models models, AppControllers appControllers) {
        this.navigator = navigator;
        this.models = models;
        this.appControllers = appControllers;
    }

    public FxHomeView createFxHomeView() {
        FxHomeViewController graphicController = new FxHomeViewController(navigator, this);
        return new FxHomeView(graphicController, models.userModel());
    }

    public FxAuthView createFxAuthView() {
        FxAuthViewController graphicController = new FxAuthViewController(navigator);
        return new FxAuthView(graphicController, models.userModel(), this::createFxAuthLoginView, this::createFxAuthRegisterView);
    }

    public FxAuthLoginView createFxAuthLoginView() {
        FxAuthLoginViewController graphicController = new FxAuthLoginViewController(appControllers.authController());
        return new FxAuthLoginView(graphicController);
    }

    public FxAuthRegisterView createFxAuthRegisterView() {
        FxAuthRegisterViewController graphicController = new FxAuthRegisterViewController(appControllers.authController());
        return new FxAuthRegisterView(graphicController);
    }
}
