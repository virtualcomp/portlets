package sk.vic.liferay.faces.beans;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import sk.vic.liferay.faces.utils.FacesMessageUtil;
import com.liferay.faces.portal.context.LiferayFacesContext;
import com.liferay.faces.util.logging.Logger;
import com.liferay.faces.util.logging.LoggerFactory;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.model.User;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.liferay.portal.theme.ThemeDisplay;

@ManagedBean(name = "usersModelBean")
@SessionScoped
public class UsersModelBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6874519504116134039L;
	private static final Logger logger = LoggerFactory.getLogger(UsersModelBean.class);
	private final LiferayFacesContext liferayFacesContext = LiferayFacesContext.getInstance();
	private User selectedUser = null;
	private String emailAddress = null;
	private transient String selectedUserPortraitURL;

	// Private Data Members
	private Long companyId = null;

	public User getSelectedUser() {

		if (selectedUser == null) {
			try {
				selectedUser = UserLocalServiceUtil.fetchUserByEmailAddress(getCompanyId(), getEmailAddress());
			} catch (SystemException e) {
				logger.error(e.getMessage(), e);
				liferayFacesContext.addGlobalErrorMessage("User was not found for such email.");
				liferayFacesContext.addGlobalUnexpectedErrorMessage();
			}
		}
		return selectedUser;
	}

	public void setSelectedUser(User selectedUser) {
		this.selectedUser = selectedUser;
	}

	public Long getCompanyId() {
		if (companyId == null) {
			companyId = liferayFacesContext.getCompanyId();
		}
		logger.debug("getCompanyId() = " + companyId);
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	public String getSelectedUserPortraitURL() {
		if (selectedUserPortraitURL == null) {
			try {
				// FacesContext facesContext =
				// FacesContext.getCurrentInstance();
				// ExternalContext externalContext =
				// facesContext.getExternalContext();
				// PortletRequest portletRequest = (PortletRequest)
				// externalContext.getRequest();
				// ThemeDisplay themeDisplay = (ThemeDisplay)
				// portletRequest.getAttribute(WebKeys.THEME_DISPLAY);
				ThemeDisplay themeDisplay = liferayFacesContext.getThemeDisplay();
				selectedUserPortraitURL = selectedUser.getPortraitURL(themeDisplay);
			} catch (PortalException e1) {
				logger.error(e1.getMessage(), e1);
				liferayFacesContext.addGlobalUnexpectedErrorMessage();
			} catch (Exception e1) {
				logger.error(e1.getMessage(), e1);
				liferayFacesContext.addGlobalUnexpectedErrorMessage();
			}

		}

		return selectedUserPortraitURL;
	}

	public void setSelectedUserPortraitURL(String selectedUserPortraitURL) {
		this.selectedUserPortraitURL = selectedUserPortraitURL;
	}

	public String getEmailAddress() {
		if (emailAddress == null) {
			emailAddress = "juraj.kral@liferay.com";
		}

		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		// logger.info("SET Email was changed : " + emailAddress);
		this.emailAddress = emailAddress;
	}

	public void emailSubmit() {
		setSelectedUser(null);
		setSelectedUserPortraitURL(null);

		if (getSelectedUser() != null) {
			logger.info("Email was changed to: " + emailAddress);
			liferayFacesContext.addGlobalInfoMessage("Email was changed to: " + emailAddress);
		} else {
			liferayFacesContext.addGlobalErrorMessage("User was not found for such email.");
		}

	}

}
