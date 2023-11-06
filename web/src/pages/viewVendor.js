import VendorEventClient from '../api/vendorEventClient';
import Header from '../components/header';
import BindingClass from '../util/bindingClass';
import DataStore from '../util/DataStore';

class ViewVendor extends BindingClass {
    constructor() {
        super();
        this.bindClassMethods(['clientLoaded', 'mount', 'displayVendor','showLoading', 'hideLoading'], this);
        this.dataStore = new DataStore();
        this.header = new Header(this.dataStore);
        this.client = new VendorEventClient();

        console.log("viewVendor constructor");
    }

    mount() {
        this.header.addHeaderToPage();
        this.clientLoaded();
    }

    async clientLoaded() {
        this.showLoading();

        const urlParams = new URLSearchParams(window.location.search);
        const id = urlParams.get('id');
        const name = urlParams.get('name');

        const result = await this.client.getVendor(id, name);
        this.hideLoading();

        console.log("Result:", result);
        this.dataStore.set('vendor', result);

        this.displayVendor();

    }

    showLoading() {
        document.getElementById('vendors-loading').innerText = "(Loading Vendor...)";
    }

    hideLoading() {
            document.getElementById('vendors-loading').style.display = 'none';
    }

    displayVendor() {
        const vendor = this.dataStore.get('vendor');
        const displayDiv = document.getElementById('vendors-list-display');
        const displayName = document.getElementById('display-name');

        const nameCard = document.createElement('section');
        nameCard.className = 'card';

        const bioCard = document.createElement('section');
        bioCard.className = 'singleVendorCard';

        const tagsCard = document.createElement('section');
        tagsCard.className = 'singleVendorCard';

        const vendorName = document.createElement('h1')
        vendorName.innerText = vendor.name;

        const vendorBio = document.createElement('h3');
        vendorBio.innerText = "Bio: "+vendor.bio;

        const vendorTags = document.createElement('h3');
        vendorTags.innerText = vendor.tags.join(', ');


        bioCard.appendChild(vendorBio);
        nameCard.appendChild(vendorName);
        tagsCard.appendChild(vendorTags)

        displayName.appendChild(nameCard);
        displayDiv.appendChild(bioCard);
        displayDiv.appendChild(tagsCard);
    }

}
const main = async () => {
    const viewVendor = new ViewVendor();
    viewVendor.mount();
};

window.addEventListener('DOMContentLoaded', main);