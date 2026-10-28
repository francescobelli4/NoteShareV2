package app.appcontrollers;

import app.beans.LoginBean;
import app.beans.RegisterBean;

public class AuthController {

    public AuthController() {
        // Nothing to do
    }

    public void performLogin(LoginBean loginBean) {
        System.out.println("LOGIN " + loginBean.username() + " " + loginBean.password());
    }

    public void performRegister(RegisterBean registerBean) {
        System.out.println("REG " + registerBean.username() + " " + registerBean.password() + " " + registerBean.role());
    }
}
