package com.manydesigns.portofino.actions;

import com.manydesigns.portofino.stripes.AbstractActionBean;

public class GAELoginAction extends AbstractActionBean {
//    public static final Logger logger = LoggerFactory.getLogger(GAELoginAction.class);
//
//    protected String returnUrl;
//
//    @DefaultHandler
//    public Resolution login() {
//        Subject subject = SecurityUtils.getSubject();
//        if (!subject.isAuthenticated()) {
//            logger.debug("User not authenticated, redirecting to GAE login URL");
//            UserService userService = UserServiceFactory.getUserService();
//            String loginUrl = userService.createLoginURL(returnUrl);
//            return new RedirectResolution(loginUrl);
//        } else {
//            return new RedirectResolution(returnUrl);
//        }
//    }
//
//    public Resolution logout() {
//        Subject subject = SecurityUtils.getSubject();
//        if(subject.isAuthenticated()) {
//            logger.debug("User not authenticated, redirecting to GAE logout URL");
//            UserService userService = UserServiceFactory.getUserService();
//            Serializable userId = ShiroUtils.getUserId(SecurityUtils.getSubject());
//            SecurityUtils.getSubject().logout();
//            HttpSession session = context.getRequest().getSession(false);
//            if (session != null) {
//                session.invalidate();
//            }
//
//            String msg = ElementsThreadLocals.getText("user.disconnected");
//            SessionMessages.addInfoMessage(msg);
//            logger.info("User {} logout", userId);
//            String logoutUrl = userService.createLogoutURL(context.getRequest().getContextPath() + "/");
//            return new RedirectResolution(logoutUrl);
//        } else {
//            return new RedirectResolution("/");
//        }
//    }
//
//    public String getReturnUrl() {
//        return returnUrl;
//    }
//
//    public void setReturnUrl(String returnUrl) {
//        this.returnUrl = returnUrl;
//    }
}