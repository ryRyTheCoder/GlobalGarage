import GlobalGarageClient from '../api/globalGarageClient';
import Header from '../components/header';
import BindingClass from '../util/bindingClass';
import DataStore from '../util/DataStore';

class MyBuyerAccount extends BindingClass {
    constructor() {
        super();
        this.bindClassMethods([
            'mount',
            'loadBuyerData',
            'displayBuyer',
            'loadInterestedItemsDetails',
            'displayItemDetails',
            'showLoading',
            'hideLoading',
            'addUpdateBuyerButtonListener',
            'showUpdateBuyerModal',
            'handleUpdateBuyerFormSubmit',
            'displayNoItemsMessage'
        ], this);
        this.dataStore = new DataStore();
        this.header = new Header(this.dataStore);
        this.client = new GlobalGarageClient();

        console.log("MyBuyerAccount constructor");
    }

    async mount() {
        this.header.addHeaderToPage();

        const urlParams = new URLSearchParams(window.location.search);
        const buyerId = urlParams.get('buyerId');
        this.dataStore.set('buyerId', buyerId);

        const currentUser = await this.header.client.getIdentity();
        if (currentUser && buyerId === "B" + currentUser.sub) {
            // The current user's ID matches the buyerId in the URL parameters
            this.loadBuyerData(buyerId);
            this.addUpdateBuyerButtonListener();
        } else {
            // Handle the situation where the user is not authorized to view this page
            console.error("Unauthorized access attempt to buyer account");
            window.location.href = 'index.html';
        }
    }

    async loadBuyerData(buyerId) {
        this.showLoading();

        try {
            const result = await this.client.getBuyer(buyerId);
            this.hideLoading();
            console.log("Result:", result);
            this.dataStore.set('buyer', result.buyerModel);
            this.dataStore.set('itemsInterested', result.buyerModel.itemsInterested);
            this.displayBuyer();
            this.loadInterestedItemsDetails();  // Call this method here
        } catch (error) {
            console.error("Error loading buyer data:", error);
            this.hideLoading();
            // Handle error (e.g., show error message)
        }
    }

    async loadInterestedItemsDetails() {
        const itemsInterested = this.dataStore.get('itemsInterested');
            if (itemsInterested.length === 0) {
                // If there are no interested items, display a message
                this.displayNoItemsMessage();
            } else {
        for (const concatenatedId of itemsInterested) {
            const [itemId, garageId] = concatenatedId.split(":");
            try {
                const itemDetails = await this.client.getItem(itemId, garageId);
                this.displayItemDetails(itemDetails.itemModel);
            } catch (error) {
                console.error(`Error loading details for item ${itemId}:`, error);
                // Handle error
            }
        }
    }
    }

    displayNoItemsMessage() {
        const itemsDisplayDiv = document.getElementById('interested-items-display');
        itemsDisplayDiv.innerHTML = 'No items to show';
        }

    displayBuyer() {
        const buyer = this.dataStore.get('buyer');
        const displayDiv = document.getElementById('buyer-display');

        const buyerCard = document.createElement('section');
        buyerCard.className = 'buyerCard';

        // Display Username
        const buyerUsername = document.createElement('h1');
        buyerUsername.innerText = "Username: " + buyer.username;
        buyerCard.appendChild(buyerUsername);

        // Display Email
        const buyerEmail = document.createElement('h3');
        buyerEmail.innerText = "Email: " + buyer.email;
        buyerCard.appendChild(buyerEmail);

        // Display Location
        const buyerLocation = document.createElement('h3');
        buyerLocation.innerText = "Location: " + buyer.location;
        buyerCard.appendChild(buyerLocation);

        displayDiv.appendChild(buyerCard);
    }

    displayItemDetails(itemDetails) {
        const itemsDisplayDiv = document.getElementById('interested-items-display');



        itemDetails.forEach(item => {
            // Create item card
            const itemCard = document.createElement('div');
            itemCard.className = 'itemCard';

            // Item Image
            const itemImage = document.createElement('img');
            itemImage.src = item.imageUrl;
            itemImage.alt = item.name;
            itemCard.appendChild(itemImage);

            // Item Name
            const itemName = document.createElement('h3');
            itemName.innerText = item.name;
            itemCard.appendChild(itemName);

            // Item Price
            const itemPrice = document.createElement('p');
            itemPrice.innerText = "Price: " + item.price;
            itemCard.appendChild(itemPrice);

            // Add click event to navigate to the garage this item belongs to
            itemCard.addEventListener('click', () => {
                // Redirecting to the garage page with both garageId and sellerId in the URL
                window.location.href = `/viewOneGarage?garageId=${item.garageId}&sellerId=${item.sellerId}`;
            });

            itemsDisplayDiv.appendChild(itemCard);
        });

    }

showLoading() {
    const loadingElement = document.getElementById('buyer-loading');
    if (loadingElement) {
        loadingElement.innerText = "(Loading buyer...)";
    } else {
        console.error('Loading element not found in the DOM');
    }
}

hideLoading() {
    const loadingElement = document.getElementById('buyer-loading');
    if (loadingElement) {
         loadingElement.style.display = 'none';
    } else {
        console.error('Loading element not found in the DOM');
    }
}

    addUpdateBuyerButtonListener() {
        const updateBuyerButton = document.getElementById('updateBuyerButton');
        if (updateBuyerButton) {
            updateBuyerButton.addEventListener('click', this.showUpdateBuyerModal.bind(this));
        } else {
            console.error("Update Buyer button not found in the DOM");
        }
    }

    showUpdateBuyerModal() {
        const buyer = this.dataStore.get('buyer');
        if (!buyer) {
            console.error('No buyer data found');
            return;
        }

        const modalContainer = document.createElement('div');
        modalContainer.className = 'modal-container';
        modalContainer.id = 'updateBuyerModalContainer';

        modalContainer.innerHTML = `
            <div class="modal">
                <span class="close-modal" onclick="this.parentElement.parentElement.remove()">&times;</span>
                <form id="updateBuyerForm" class="modal-form">
                    <div class="form-group">
                        <label for="username">Username:</label>
                        <input type="text" id="username" name="username" value="${buyer.username}">
                    </div>
                    <div class="form-group">
                        <label for="email">Email:</label>
                        <input type="email" id="email" name="email" value="${buyer.email}">
                    </div>
                    <div class="form-group">
                        <label for="location">Location:</label>
                        <input type="text" id="location" name="location" value="${buyer.location}">
                    </div>
                    <!-- Add or remove fields as per your Buyer model -->
                    <button type="submit">Update</button>
                </form>
            </div>
        `;

        document.body.appendChild(modalContainer);

        document.getElementById('updateBuyerForm').addEventListener('submit', this.handleUpdateBuyerFormSubmit.bind(this));
    }

    async handleUpdateBuyerFormSubmit(event) {
        event.preventDefault();
        console.log("Handling form submission for updating buyer");

        const formData = new FormData(event.target);
        const updatedBuyerDetails = {
            username: formData.get('username'),
            email: formData.get('email'),
            location: formData.get('location')

        };

        const buyerId = this.dataStore.get('buyerId');
        try {
            const updateResponse = await this.client.updateBuyer(buyerId, updatedBuyerDetails);
            console.log('Update response:', updateResponse);
            window.location.reload();
        } catch (error) {
            console.error('Error updating buyer:', error);
            // Handle error (e.g., show error message in modal)
        }
    }
    }


const main = async () => {
    const myBuyerAccount = new MyBuyerAccount();
    myBuyerAccount.mount();
};

window.addEventListener('DOMContentLoaded', main);
