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
        /**
         * Formats a date string to a more readable format.
         * @param {string} isoString - ISO date string to format.
         * @returns {string} Formatted date string.
         */
        static formatDateTime(isoString) {
            const options = { month: 'long', day: 'numeric', year: 'numeric', hour: '2-digit', minute: '2-digit' };
            return new Date(isoString).toLocaleString('en-US', options);
        }


   async addHeaderToPage() {
       const currentUser = await this.client.getIdentity();
       console.log("currentUser: ", currentUser);

       let userInfo;
       if (currentUser) {
           const isSeller = await this.isSeller(currentUser.sub);
           const isBuyer = await this.isBuyer(currentUser.sub);
           userInfo = this.createUserInfoForHeader(currentUser, isSeller, isBuyer, currentUser.sub);
       } else {
           userInfo = this.createUserInfoForHeader(null, false, false, null);
       }

       const siteTitle = this.createSiteTitle();
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

  createUserInfoForHeader(currentUser, isSeller, isBuyer, userId) {
        const userInfo = document.createElement('div');
        userInfo.classList.add('user');

        if (currentUser) {
            userInfo.appendChild(this.createLogoutButton(currentUser));

         if (isSeller) {
                    userInfo.appendChild(this.createMySellerAccountButton("S" + userId));
                } else {
                userInfo.appendChild(this.createStartSellingButton());
            }

          if (isBuyer) {
                userInfo.appendChild(this.createMyBuyerAccountButton("B" + userId));
            } else {
                userInfo.appendChild(this.createBecomeBuyerButton());
            }
        } else {
            userInfo.appendChild(this.createLoginButton());
        }

        return userInfo;
    }

   createMySellerAccountButton(userId) {
        const button = document.createElement('a');
        button.classList.add('button');
        button.href = `mySellerAccount.html?sellerId=${userId}`;
        button.innerText = 'My Seller Account';
        return button;
    }

    // Modify the href to include the userId
    createMyBuyerAccountButton(userId) {
        const button = document.createElement('a');
        button.classList.add('button');
        button.href = `myBuyerAccount.html?buyerId=${userId}`;
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
