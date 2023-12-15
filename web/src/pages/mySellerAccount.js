import GlobalGarageClient from '../api/globalGarageClient';
import Header from '../components/header';
import BindingClass from '../util/bindingClass';
import DataStore from '../util/DataStore';

class MySellerAccount extends BindingClass {
    constructor() {
        super();
        this.bindClassMethods(['mount', 'loadSellerData', 'displaySeller', 'loadSellerGarages', 'displayGarages', 'showLoading',  'hideLoading', 'redirectToCreateGarage', 'showUpdateSellerModal', 'handleUpdateSellerFormSubmit'], this);
        this.dataStore = new DataStore();
        this.header = new Header(this.dataStore);
        this.client = new GlobalGarageClient();

        console.log("MySellerAccount constructor");
    }

    mount() {
        this.header.addHeaderToPage();

        const urlParams = new URLSearchParams(window.location.search);
        const sellerId = urlParams.get('sellerId');
        this.dataStore.set('sellerId', sellerId);

        this.loadSellerData(sellerId);
        this.loadSellerGarages(sellerId);
        this.addCreateGarageButtonListener();
        this.addUpdateSellerButtonListener();
}
    async loadSellerData(sellerId) {
//        this.showLoading();

        try {
            const result = await this.client.getSeller(sellerId);
            this.hideLoading();
            console.log("Result:", result);
            this.dataStore.set('seller', result.sellerModel);
            this.displaySeller();
        } catch (error) {
            console.error("Error loading seller data:", error);
            this.hideLoading();
            // Handle error (e.g., show error message)
        }
    }
    showLoading() {
          document.getElementById('seller-loading').innerText = "(Loading seller...)";
    }

    hideLoading() {
      document.getElementById('seller-loading').style.display = 'none';
    }
    async loadSellerGarages(sellerId) {
        if (!sellerId) {
            console.error('Seller ID not found for loading garages');
            return;
        }

        try {
            const garages = await this.client.getGaragesBySeller(sellerId);
            this.displayGarages(garages.garageModels);
        } catch (error) {
            console.error("Error loading garages:", error);
            // Handle error (e.g., show error message)
        }
    }

displayGarages(garages) {
    const garagesListContainer = document.getElementById('garage-list-container');
    garagesListContainer.innerHTML = ''; // Clear the current list

    // Ensure the container uses a flexbox or grid layout
    garagesListContainer.style.display = 'flex';
    garagesListContainer.style.flexWrap = 'wrap'; // Allows wrapping to the next line
    garagesListContainer.style.justifyContent = 'space-around'; // Evenly spaces children

    garages.forEach(garage => {
        const garageElement = document.createElement('div');
        garageElement.className = 'garage';
        garageElement.style.flex = '0 1 calc(33% - 1em)'; // Flex grow, shrink, and basis

        garageElement.innerHTML = `
            <h3>${garage.garageName}</h3>
            <p>Start Date: ${Header.formatDateTime(garage.startDate)}</p>
            <p>End Date: ${Header.formatDateTime(garage.endDate)}</p>
            <p>Location: ${garage.location}</p>
            <p>Description: ${garage.description}</p>
        `;

        garageElement.addEventListener('click', () => {
            window.location.href = `viewGarage.html?sellerId=${encodeURIComponent(garage.sellerID)}&garageId=${encodeURIComponent(garage.garageID)}`;
        });

        garagesListContainer.appendChild(garageElement);
    });
}


    displaySeller() {
        const seller = this.dataStore.get('seller');
        const displayDiv = document.getElementById('seller-display');

        const sellerCard = document.createElement('section');
        sellerCard.className = 'sellerCard';

        const sellerUsername = document.createElement('h1');
        sellerUsername.innerText = "Username: " + seller.username;

        const sellerEmail = document.createElement('h3');
        sellerEmail.innerText = "Email: " + seller.email;

        const sellerLocation = document.createElement('h3');
        sellerLocation.innerText = "Location: " + seller.location;

        const sellerContactInfo = document.createElement('h3');
        sellerContactInfo.innerText = "Contact Info: " + seller.contactInfo;

        sellerCard.appendChild(sellerUsername);
        sellerCard.appendChild(sellerEmail);
        sellerCard.appendChild(sellerLocation);
        sellerCard.appendChild(sellerContactInfo);

        displayDiv.appendChild(sellerCard);
    }

    addCreateGarageButtonListener() {
        const createGarageButton = document.getElementById('createGarageButton');
        if (createGarageButton) {
            createGarageButton.addEventListener('click', this.redirectToCreateGarage);
        } else {
            console.error("Create Garage button not found in the DOM");
        }
    }

    redirectToCreateGarage() {
        const sellerId = this.dataStore.get('sellerId');
        if (sellerId) {
            window.location.href = `createGarage.html?sellerId=${sellerId}`;
        } else {
            console.error('Seller ID not found');
        }
    }
    addUpdateSellerButtonListener() {
        const updateSellerButton = document.getElementById('updateSellerButton');
        if (updateSellerButton) {
            updateSellerButton.addEventListener('click', this.showUpdateSellerModal);
        } else {
            console.error("Update Seller button not found in the DOM");
        }
    }


     showUpdateSellerModal() {
         const seller = this.dataStore.get('seller');
         if (!seller) {
             console.error('No seller data found');
             return;
         }

         // Create and show the modal
         const modalContainer = document.createElement('div');
         modalContainer.className = 'modal-container';
         modalContainer.id = 'updateSellerModalContainer';

         modalContainer.innerHTML = `
             <div class="modal">
                 <span class="close-modal" onclick="this.parentElement.parentElement.remove()">&times;</span>
                 <form id="updateSellerForm" class="modal-form">
                     <div class="form-group">
                         <label for="username">Username:</label>
                         <input type="text" id="username" name="username" value="${seller.username}">
                     </div>
                     <div class="form-group">
                         <label for="email">Email:</label>
                         <input type="email" id="email" name="email" value="${seller.email}">
                     </div>
                     <div class="form-group">
                         <label for="location">Location:</label>
                         <input type="text" id="location" name="location" value="${seller.location}">
                     </div>
                     <div class="form-group">
                         <label for="contactInfo">Contact Info:</label>
                         <input type="text" id="contactInfo" name="contactInfo" value="${seller.contactInfo}">
                     </div>
                     <button type="submit">Update</button>
                 </form>
             </div>
         `;

         // Append the modal container to the body or a specific div in your HTML
         document.body.appendChild(modalContainer);

         // Now you can add the event listener to the form in the modal
         document.getElementById('updateSellerForm').addEventListener('submit', this.handleUpdateSellerFormSubmit.bind(this));
     }

     async handleUpdateSellerFormSubmit(event) {
         event.preventDefault();
         console.log("Handling form submission for updating seller");

         const formData = new FormData(event.target);
         const updatedSellerDetails = {
             username: formData.get('username'),
             email: formData.get('email'),
             location: formData.get('location'),
             contactInfo: formData.get('contactInfo')
         };

         const sellerId = this.dataStore.get('sellerId');
         try {
             const updateResponse = await this.client.updateSeller(sellerId, updatedSellerDetails);
             console.log('Update response:', updateResponse);
             window.location.reload();
         } catch (error) {
             console.error('Error updating seller:', error);
             // Handle error (e.g., show error message in modal)
         }
     }

}

const main = async () => {
    const mySellerAccount = new MySellerAccount();
    mySellerAccount.mount();

     const messagesCard = document.getElementById('my-messages-card');
     messagesCard.style.display = 'none';

};

window.addEventListener('DOMContentLoaded', main);
