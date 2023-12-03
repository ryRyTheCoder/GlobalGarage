import GlobalGarageClient from '../api/globalGarageClient';
import BindingClass from "../util/bindingClass";

/**
 * The header component for the website.
 */
export default class Header extends BindingClass {
    constructor() {
        super();

        const methodsToBind = [
            'addHeaderToPage', 'createSiteTitle', 'createUserInfoForHeader',
            'createLoginButton', 'createLogoutButton', 'createMySellerAccountButton',
            'createMyBuyerAccountButton', 'createBecomeBuyerButton', 'createStartSellingButton',
            'createButton', 'isSeller', 'isBuyer'
        ];
        this.bindClassMethods(methodsToBind, this);

        this.client = new GlobalGarageClient();
    }

    async addHeaderToPage() {
        const currentUser = await this.client.getIdentity();
          console.log("currentUser: ", currentUser);
        const isSeller = await this.isSeller(currentUser.sub);
       
        const isBuyer = await this.isBuyer(currentUser.sub);

        const siteTitle = this.createSiteTitle();
        const userInfo = this.createUserInfoForHeader(currentUser, isSeller, isBuyer);

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

    async isSeller(userId) {
        try {
            console.log("UserID: ", userId);
            const seller = await this.client.getSeller("S" + userId);
            return !!seller;
        } catch (error) {
            return false;
        }
    }

    async isBuyer(userId) {
        try {
            const buyer = await this.client.getBuyer("B" + userId);
            return !!buyer;
        } catch (error) {
            return false;
        }
    }

    createUserInfoForHeader(currentUser, isSeller, isBuyer) {
        const userInfo = document.createElement('div');
        userInfo.classList.add('user');

        if (currentUser) {
            userInfo.appendChild(this.createLogoutButton(currentUser));

            if (isSeller) {
                userInfo.appendChild(this.createMySellerAccountButton());
            } else {
                userInfo.appendChild(this.createStartSellingButton());
            }

            if (isBuyer) {
                userInfo.appendChild(this.createMyBuyerAccountButton());
            } else {
                userInfo.appendChild(this.createBecomeBuyerButton());
            }
        } else {
            userInfo.appendChild(this.createLoginButton());
        }

        return userInfo;
    }

    createMySellerAccountButton() {
        const button = document.createElement('a');
        button.classList.add('button');
        button.href = 'MySellerAccount.html';
        button.innerText = 'My Seller Account';
        return button;
    }

    createMyBuyerAccountButton() {
        const button = document.createElement('a');
        button.classList.add('button');
        button.href = 'MyBuyerAccount.html';
        button.innerText = 'My Buyer Account';
        return button;
    }

    createBecomeBuyerButton() {
        const button = document.createElement('a');
        button.classList.add('button');
        button.href = 'createBuyer.html';
        button.innerText = 'Become a Buyer';
        button.style.color = "#F9AA33";
        return button;
    }

    createStartSellingButton() {
        const button = document.createElement('a');
        button.classList.add('button');
        button.href = 'createSeller.html';
        button.innerText = 'Start Selling';
        button.style.color = "#F9AA33";
        return button;
    }

    createLoginButton() {
        return this.createButton('Login', this.client.login);
    }

    createLogoutButton(currentUser) {
        return this.createButton(`Logout: ${currentUser.name}`, this.client.logout);
    }

    createButton(text, clickHandler) {
        const button = document.createElement('a');
        button.classList.add('button');
        button.href = '#';
        button.innerText = text;
        button.addEventListener('click', async () => await clickHandler());
        return button;
    }
}
