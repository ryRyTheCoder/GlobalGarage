import GlobalGarageClient from '../api/globalGarageClient';
import Header from '../components/header';
import BindingClass from '../util/bindingClass';
import DataStore from '../util/DataStore';

class ViewGarage extends BindingClass {
    constructor() {
        super();
        this.bindClassMethods(['clientLoaded', 'mount', 'displayGarage', 'showLoading', 'hideLoading', 'openModal', 'likeItem', 'displayItems', 'deleteItem', 'createGarageCard', 'showCreateItemModal', 'createNewItem'], this);
        this.dataStore = new DataStore();
        this.header = new Header(this.dataStore);
        this.client = new GlobalGarageClient();

        console.log("ViewGarage constructor");
    }

    mount() {
        this.header.addHeaderToPage();
        this.clientLoaded();
    }

    async clientLoaded() {
        this.showLoading();

        const urlParams = new URLSearchParams(window.location.search);
        const sellerId = urlParams.get('sellerId');
        const garageId = urlParams.get('garageId');

  try {
            const result = await this.client.getOneGarage(sellerId, garageId);
            console.log("GetOneGarage result:", result);
            this.dataStore.set('garage', result);
            this.displayGarage();
        } catch (error) {
            console.error("Error in GetOneGarage call:", error);
        } finally {
            this.hideLoading();
        }
    }


    showLoading() {
        document.getElementById('garage-loading').innerText = "(Loading Garage...)";
    }

    hideLoading() {
        document.getElementById('garage-loading').style.display = 'none';
    }
    async deleteItem(garageId, itemId) {
        try {
            // API call to delete the item
            await this.client.deleteItem(garageId, itemId);
            console.log('Item deleted:', itemId);
            // Hide the modal and refresh the garage view
            document.getElementById('item-modal').style.display = 'none';
            this.clientLoaded(); // Reload the garage details and items
        } catch (error) {
            console.error('Error deleting item:', error);
        }
    }

    async displayGarage() {
        const garage = this.dataStore.get('garage').garageModel;
        const garageDisplayDiv = document.getElementById('garage-display');
        const itemsDisplayDiv = document.getElementById('items-display');

        garageDisplayDiv.innerHTML = ''; // Clear existing content
        itemsDisplayDiv.innerHTML = ''; // Clear existing content

        // Garage information
        const garageCard = this.createGarageCard(garage);
        garageDisplayDiv.appendChild(garageCard);

        const currentUser = await this.header.client.getIdentity();
        if (currentUser && await this.header.isSeller(currentUser.sub) && garage.sellerID === "S" + currentUser.sub) {
            const createItemButton = document.createElement('button');
            createItemButton.innerText = 'Create Item';
              createItemButton.onclick = () => this.showCreateItemModal(garage.garageID, "S" + currentUser.sub);
            garageDisplayDiv.appendChild(createItemButton);
        }

        // Items display
        await this.displayItems(garage.garageID, garage.items, itemsDisplayDiv);
    }

    createGarageCard(garage) {
        const garageCard = document.createElement('section');
        garageCard.className = 'garageCard';

        const garageName = document.createElement('h1');
        garageName.innerText = garage.garageName;

        const garageDates = document.createElement('h3');
        garageDates.innerText = `Start Date: ${garage.startDate} - End Date: ${garage.endDate}`;

        const garageLocation = document.createElement('h3');
        garageLocation.innerText = "Location: " + garage.location;

        const garageDescription = document.createElement('p');
        garageDescription.innerText = garage.description;

        garageCard.appendChild(garageName);
        garageCard.appendChild(garageDates);
        garageCard.appendChild(garageLocation);
        garageCard.appendChild(garageDescription);

        return garageCard;
    }

    async displayItems(garageId, itemIds, itemsDisplayDiv) {
        const itemsContainer = document.createElement('div');
        itemsContainer.id = 'items-container';
        itemsContainer.className = 'items-container';

 // Check if there are no items or the items array is empty
    if (!itemIds || itemIds.length === 0) {
        const noItemsMessage = document.createElement('p');
        noItemsMessage.innerText = "No items to display";
        noItemsMessage.className = 'no-items-message';
        itemsContainer.appendChild(noItemsMessage);
        } else {
            for (const itemId of itemIds) {
                try {
                    const itemDetails = await this.getItemDetails(garageId, itemId);
                    if (itemDetails) {
                        const itemCard = this.createItemCard(itemDetails);
                        itemsContainer.appendChild(itemCard);
                    }
                } catch (error) {
                    console.error(`Error fetching details for item ${itemId}:`, error);
                }
            }
        }

        itemsDisplayDiv.appendChild(itemsContainer);
    }

    async getItemDetails(garageId, itemId) {
        try {
            const response = await this.client.getItem(garageId, itemId);
            return response && response.success ? response.itemModel : null;
        } catch (error) {
            console.error(`Error fetching item with ID ${itemId}:`, error);
            return null;
        }
    }

    createItemCard(itemDetails) {
        const itemCard = document.createElement('div');
        itemCard.className = 'item-card';
        itemCard.onclick = () => this.openModal(itemDetails);

        const itemName = document.createElement('h4');
        itemName.innerText = itemDetails.name;

        const itemPrice = document.createElement('p');
        itemPrice.innerText = `Price: ${itemDetails.price}`;

        const itemImage = document.createElement('img');
        itemImage.src = itemDetails.imageUrl;
        itemImage.alt = itemDetails.name;

        itemCard.appendChild(itemImage);
        itemCard.appendChild(itemName);
        itemCard.appendChild(itemPrice);

        return itemCard;
    }

async openModal(itemDetails) {
 const modalContainer = document.getElementById('item-modal');
    const modalContent = document.getElementById('modal-content');
    modalContent.innerHTML = ''; // Clear previous content


    // Item Name
    const itemName = document.createElement('h2');
    itemName.innerText = itemDetails.name;
    modalContent.appendChild(itemName);

    // Item Image
    if (itemDetails.images && itemDetails.images.length > 0) {
        const itemImage = document.createElement('img');
        itemImage.src = itemDetails.images[0]; // Displaying the first image
        itemImage.alt = itemDetails.name;
        modalContent.appendChild(itemImage);
    }

    // Item Description
    const itemDescription = document.createElement('p');
    itemDescription.innerText = itemDetails.description;
    modalContent.appendChild(itemDescription);

    // Item Price
    const itemPrice = document.createElement('p');
    itemPrice.innerText = `Price: $${itemDetails.price}`;
    modalContent.appendChild(itemPrice);

    // Item Category
    const itemCategory = document.createElement('p');
    itemCategory.innerText = `Category: ${itemDetails.category}`;
    modalContent.appendChild(itemCategory);

    // Item Status
    const itemStatus = document.createElement('p');
    itemStatus.innerText = `Status: ${itemDetails.status}`;
    modalContent.appendChild(itemStatus);

    // Item Date Listed
    const itemDateListed = document.createElement('p');
    itemDateListed.innerText = `Date Listed: ${itemDetails.dateListed}`;
    modalContent.appendChild(itemDateListed);

    // Like Button
    const likeButton = document.createElement('button');
    likeButton.innerText = '❤ Like';
    likeButton.onclick = () => this.likeItem(itemDetails.itemID);
    modalContent.appendChild(likeButton);

       const currentUser = await this.header.client.getIdentity();
       if (currentUser) {
           if (itemDetails.sellerID === "S" + currentUser.sub) {
               // Add delete button
               const deleteButton = document.createElement('button');
               deleteButton.innerText = 'Delete Item';
               deleteButton.onclick = () => this.deleteItem(itemDetails.garageID, itemDetails.itemID);
               modalContent.appendChild(deleteButton);
           }
       }


    // Display Modal
    document.getElementById('item-modal').style.display = 'block';
      modalContainer.style.display = 'flex';
}

    likeItem(itemId) {
        // API call to like the item
        console.log('Liking item:', itemId);
        // Update the 'buyersInterested' attribute for the item
    }
    showCreateItemModal(garageId) {
        const modalContainer = document.getElementById('item-modal');
        const modalContent = document.getElementById('modal-content');
        modalContent.innerHTML = ''; // Clear previous content

        // Create form elements
        const form = document.createElement('form');
        form.id = 'create-item-form';
        form.innerHTML = `
            <div class="form-group">
                <label for="itemName">Name:</label>
                <input type="text" id="itemName" name="itemName" required>
            </div>
            <div class="form-group">
                <label for="itemDescription">Description:</label>
                <textarea id="itemDescription" name="itemDescription" required></textarea>
            </div>
            <div class="form-group">
                <label for="itemPrice">Price:</label>
                <input type="number" id="itemPrice" name="itemPrice" step="0.01" required>
            </div>
            <div class="form-group">
                <label for="itemCategory">Category:</label>
                <input type="text" id="itemCategory" name="itemCategory" required>
            </div>
            <div class="form-group">
                <label for="itemImages">Images (URLs, comma-separated):</label>
                <input type="text" id="itemImages" name="itemImages">
            </div>
            <button type="submit" class="btn btn-primary">Create Item</button>
        `;

        // Event listener for form submission
        form.addEventListener('submit', (e) => {
            e.preventDefault();
            this.createNewItem(garageId);
        });

        modalContent.appendChild(form);
        modalContainer.style.display = 'flex';
    }

    async createNewItem(garageId) {
        const form = document.getElementById('create-item-form');

        const itemName = form.itemName.value;
        const itemDescription = form.itemDescription.value;
        const itemPrice = form.itemPrice.value;
        const itemCategory = form.itemCategory.value;
        const itemImages = form.itemImages.value.split(',').map(url => url.trim()); // Split URLs by commas and trim whitespace

        try {
            const newItem = {
                name: itemName,
                description: itemDescription,
                price: parseFloat(itemPrice),
                category: itemCategory,
                images: itemImages,
            };

            // API call to create the item
            await this.client.createItem(garageId, newItem);
            console.log('Item created:', newItem);

            // Close modal and refresh
            document.getElementById('item-modal').style.display = 'none';
            this.clientLoaded();
        } catch (error) {
            console.error('Error creating new item:', error);
        }
    }

}

const main = async () => {
    const viewGarage = new ViewGarage();
    viewGarage.mount();
};

window.addEventListener('DOMContentLoaded', main);