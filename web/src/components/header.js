 import vendorEventClient from '../api/vendorEventClient';
import BindingClass from "../util/bindingClass";

/**
 * The header component for the website.
 */
export default class Header extends BindingClass {
    constructor() {
        super();

        const methodsToBind = [
            'addHeaderToPage', 'createSiteTitle', 'createUserInfoForHeader',
            'createLoginButton', 'createLogoutButton','createMyAccountButton'
        ];
        this.bindClassMethods(methodsToBind, this);

        this.client = new vendorEventClient();
    }

    /**
     * Add the header to the page.
     */
    async addHeaderToPage() {
        const currentUser = await this.client.getIdentity();

        const siteTitle = this.createSiteTitle();
        const userInfo = this.createUserInfoForHeader(currentUser);

        const header = document.getElementById('header');
        header.appendChild(siteTitle);
        header.appendChild(userInfo);
    }

    createSiteTitle() {
        const homeButton = document.createElement('a');
        homeButton.classList.add('header_home');
        homeButton.href = 'index.html';
        homeButton.innerText = 'GlobalGarage';

        const siteTitle = document.createElement('div');
        siteTitle.classList.add('site-title');
        siteTitle.appendChild(homeButton);

        return siteTitle;
    }

createUserInfoForHeader(currentUser) {
    const userInfo = document.createElement('div');
    userInfo.classList.add('user');

    if (currentUser) {
        const logoutButton = this.createLogoutButton(currentUser);
        userInfo.appendChild(logoutButton);

        const createSellerButton = this.createSellerAccountButton(currentUser);
                        userInfo.appendChild(createSellerButton);

        const createBuyerButton = this.createBuyerAccountButton(currentUser);
                        userInfo.appendChild(createBuyerButton);

        const myAccountButton = this.createMyAccountButton();
        userInfo.appendChild(myAccountButton);


    } else {

        const loginButton = this.createLoginButton();
        userInfo.appendChild(loginButton);
    }

    return userInfo;
}


    createLoginButton() {
        return this.createButton('Login', this.client.login);
    }

    createLogoutButton(currentUser) {
        return this.createButton(`Logout: ${currentUser.name}`, this.client.logout);
    }
createMyAccountButton() {
    const button = document.createElement('a');
    button.classList.add('button');
    button.href = 'VendorAccount.html';
    button.innerText = 'My Account';

    return button;
}
createSellerAccountButton() {
    const button = document.createElement('a');
    button.classList.add('button');
    button.href = 'createSeller.html';
    button.innerText = 'Start Selling';
button.style.color = "#F9AA33";
    return button;
}

createBuyerAccountButton() {
    const button = document.createElement('a');
    button.classList.add('button');
    button.href = 'createBuyer.html';
    button.innerText = 'Become a Buyer';
button.style.color = "#F9AA33";
    return button;
}

    createButton(text, clickHandler) {
        const button = document.createElement('a');
        button.classList.add('button');
        button.href = '#';
        button.innerText = text;

        button.addEventListener('click', async () => {
            await clickHandler();
        });

        return button;
    }
}
