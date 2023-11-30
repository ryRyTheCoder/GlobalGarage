import GlobalGarageClient from '../api/globalGarageClient';
import Header from '../components/header';
import BindingClass from '../util/bindingClass';
import DataStore from '../util/DataStore';

class ViewAllGarages extends BindingClass {
   constructor() {
       super();
       this.bindClassMethods(['clientLoaded', 'mount', 'displayGarages','next','previous', 'showLoading', 'hideLoading','loadGarages'], this);
       this.dataStore = new DataStore();
       this.header = new Header(this.dataStore);
       this.client = new GlobalGarageClient();
       this.dataStore.addChangeListener(this.displayGarages);
       this.previousKeys = [];
       this.currentLastEvaluatedKey = null;
       console.log("ViewAllGarages constructor");
   }

   showLoading() {
       document.getElementById('garages-loading').innerText = "(Loading garages...)";

   }

   hideLoading() {
       document.getElementById('garages-loading').style.display = 'none';
   }
   /**
    * Once the client is loaded, get the list of all garages.
    */
   async clientLoaded() {
    this.showLoading();
    await this.loadGarages();
    console.log("Garages loaded:", this.dataStore.get('garages'));
    this.hideLoading();
   }
   /**
    * Add the header to the page and load the GlobalGarageClient.
    */
   mount() {
       this.header.addHeaderToPage();
       this.clientLoaded();
       document.getElementById('nextButton').addEventListener('click', this.next);
       document.getElementById('prevButton').addEventListener('click', this.previous);
   }

async loadGarages(lastEvaluatedKey = null) {
    try {
        // Log the start of the method and the lastEvaluatedKey if provided
        console.log("loadGarages called with lastEvaluatedKey:", lastEvaluatedKey);

       const { garages, lastEvaluatedKey: newLastEvaluatedKey } = await this.client.getAllGarages(lastEvaluatedKey);


        // Log the result of the API call
        console.log("loadGarages result: Garages -", garages, ", Last Evaluated Key -", newLastEvaluatedKey);

        if (garages && garages.length > 0) {
            // Set garages and lastEvaluatedKey in the DataStore and log
            this.currentLastEvaluatedKey = newLastEvaluatedKey;
            this.dataStore.set('garages', garages);
            this.dataStore.set('lastEvaluatedKey', newLastEvaluatedKey);
            console.log("Garages and lastEvaluatedKey set in DataStore");

            // Call displayGarages to render the data
            this.displayGarages();
        } else {
            // Log error if result is not successful
            console.error("Error loading garages. Garages array is empty or undefined.");
        }
    } catch (error) {
        // Log any caught errors
        console.error("Exception in loadGarages:", error);
    }
}


   async next() {
       if (this.dataStore.get('lastEvaluatedKey')) {
           this.showLoading();
           await this.loadGarages(this.dataStore.get('lastEvaluatedKey'));
           this.hideLoading();
       }
   }

   async previous() {
       if (this.previousKeys.length > 0) {
           this.showLoading();
           const lastKey = this.previousKeys.pop();
           await this.loadGarages(lastKey);
           this.hideLoading();
       }
   }

displayGarages() {
     const garages = this.dataStore.get('garages');
     console.log("Displaying garages:", garages); // Debugging line
     const displayDiv = document.getElementById('garages-list-display');
     displayDiv.innerHTML = ''; // Clear existing content

     if (garages.length === 0) {
         displayDiv.innerText = "No more Garages available.";
         return;
     }

     garages.forEach(garage => {
         console.log("Current garage:", garage); // Debugging line

         const garageCard = document.createElement('section');
         garageCard.className = 'card';

         const garageName = document.createElement('h2');
         garageName.innerText = garage.garageName;

         const garageLocation = document.createElement('p');
         garageLocation.innerText = `Location: ${garage.location}`;

    // Extract sellerId and garageId
    const sellerId = encodeURIComponent(garage.sellerID); // Ensure property names match
    const garageId = encodeURIComponent(garage.garageID);

    // Construct the base URL
    const currentHostname = window.location.hostname;
    const isLocal = currentHostname === 'localhost' || currentHostname === '127.0.0.1';
    const baseUrl = isLocal ? 'http://localhost:8000/' : 'https://your-deployment-url.com/';

    // Construct the garage page URL
    const garagePageUrl = `${baseUrl}viewGarage.html?sellerId=${sellerId}&garageId=${garageId}`;
    console.log("Garage Page URL:", garagePageUrl); // Debugging line
         // Clickable link element
         const garageLink = document.createElement('a');
         garageLink.href = garagePageUrl;
         garageLink.innerText = "View Details";
         garageLink.className = 'garageLink';

         garageCard.appendChild(garageName);
         garageCard.appendChild(garageLocation);
         garageCard.appendChild(garageLink);

         displayDiv.appendChild(garageCard);
     });
 }
 }

const main = async () => {
   const viewAllGarages = new ViewAllGarages();
   viewAllGarages.mount();
};

window.addEventListener('DOMContentLoaded', main);

