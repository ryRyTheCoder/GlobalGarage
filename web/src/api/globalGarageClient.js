import axios from "axios";
import BindingClass from "../util/bindingClass";
import Authenticator from "./authenticator";
/**
 * Client to call the GlobalGarageService.
 */
export default class GlobalGarageClient extends BindingClass {
    constructor(props = {}) {
        super();
        const methodsToBind = ['clientLoaded', 'getIdentity', 'logout','login','getAllGarages',
        'getOneGarage', 'createSeller','createBuyer','getSeller','getBuyer', 'createGarage',
        'getGaragesBySeller', 'updateSeller', 'updateBuyer','getItem', 'deleteItem', 'createItem', 'getRecentItems'];
        this.bindClassMethods(methodsToBind, this);
        this.authenticator = new Authenticator();
        this.props = props;
        axios.defaults.baseURL = process.env.API_BASE_URL;
        this.axiosClient = axios;
        this.clientLoaded();
    }
    clientLoaded() {
        if (this.props.hasOwnProperty("onReady")) {
            this.props.onReady(this);
        }
    }

    /**
     * Get the identity of the current user
     * @param errorCallback (Optional) A function to execute if the call fails.
     * @returns The user information for the current user.
     */
    async getIdentity(errorCallback) {
        try {
            const isLoggedIn = await this.authenticator.isUserLoggedIn();

            if (!isLoggedIn) {
                return undefined;
            }

            return await this.authenticator.getCurrentUserInfo();
        } catch (error) {
            this.handleError(error, errorCallback)
        }
    }

    async login() {
        this.authenticator.login();
    }

    async logout() {
        this.authenticator.logout();
    }

    async getTokenOrThrow(unauthenticatedErrorMessage) {
        const isLoggedIn = await this.authenticator.isUserLoggedIn();
        if (!isLoggedIn) {
            throw new Error(unauthenticatedErrorMessage);
        }

        return await this.authenticator.getUserToken();
    }

    async createItem(garageId, itemDetails, errorCallback) {
        try {
            const token = await this.getTokenOrThrow("Only authenticated users can create items.");

            const payload = {
                name: itemDetails.name,
                description: itemDetails.description,
                price: itemDetails.price,
                category: itemDetails.category,
                images: itemDetails.images
            };

            // POST request to '/items/{garageId}'
            const response = await this.axiosClient.post(`/items/${garageId}`, payload, {
                headers: {
                    Authorization: `Bearer ${token}`
                }
            });

            console.log("Item created successfully:", response.data);
            return response.data;
        } catch (error) {
            console.error("Error in creating item:", error);
            this.handleError(error, errorCallback);
        }
    }
    async expressInterest(itemId, garageId) {
        try {
            // Construct the request body
            const body = { itemId, garageId };

            // Make the API call to express interest
            const response = await this.axiosClient.post('/express-interest', body);

            // Return the response from the server
            return response.data;
        } catch (error) {
            // Handle any errors that occur during the API call
            console.error('Error in expressInterest:', error);
            throw error; // Rethrow the error to be handled by the caller
        }
    }

    async deleteItem(garageId, itemId, errorCallback) {
        try {
            const token = await this.getTokenOrThrow("Only authenticated users can delete items.");

            // Construct the payload
            const payload = {
                itemId: itemId,
                garageId: garageId
            };

            // Send DELETE request with JSON body
             await this.axiosClient.delete(`/items/${garageId}/${itemId}`, {
                headers: {
                    Authorization: `Bearer ${token}`,
                    'Content-Type': 'application/json'
                },
                data: payload
            });

            console.log(`Item with ID ${itemId} deleted successfully from garage ${garageId}`);
            return { success: true, message: `Item deleted successfully` };
        } catch (error) {
            console.error(`Error deleting item with ID ${itemId}:`, error);
            this.handleError(error, errorCallback);
            return { success: false, message: `Error deleting item` };
        }
    }

    async getSeller(sellerId, errorCallback) {
        try {
            const response = await this.axiosClient.get(`/sellers/${sellerId}`);
            return response.data;
        } catch (error) {
            this.handleError(error, errorCallback);
        }
    }
    async getBuyer(buyerId, errorCallback) {
        try {
            const response = await this.axiosClient.get(`/buyers/${buyerId}`);
            return response.data;
        } catch (error) {
            this.handleError(error, errorCallback);
        }
    }

async getAllGarages(lastEvaluatedKey, errorCallback) {
    try {
        const queryParams = lastEvaluatedKey ? { next: lastEvaluatedKey } : {};
        console.log("Making API call to getAllGarages with params:", queryParams);

        const response = await this.axiosClient.get('garages', { params: queryParams });
        console.log("getAllGarages response data:", response.data);

        // Destruct the necessary data from the response
        const { garageModels, lastEvaluatedKey: newLastEvaluatedKey } = response.data;

        return {
            garages: garageModels,
            lastEvaluatedKey: newLastEvaluatedKey
        };
    } catch (error) {
        console.error("Error in getAllGarages:", error);
        this.handleError(error, errorCallback);
    }
}
async getOneGarage(sellerId, garageId, errorCallback) {
    try {
        const response = await this.axiosClient.get(`garages/${sellerId}/${garageId}`);
        return response.data;
    } catch (error) {
        this.handleError(error, errorCallback);
    }
}

async createSeller(sellerDetails, errorCallback) {
    try {
        const token = await this.getTokenOrThrow("Only authenticated users can create sellers.");

    const payload = {
            username: sellerDetails.username,
            email: sellerDetails.email,
            location: sellerDetails.location,
            contactInfo: sellerDetails.contactInfo
        };

        const response = await this.axiosClient.post('/sellers', payload, {
            headers: {
                Authorization: `Bearer ${token}`
            }
        });

        return response.data;
    } catch (error) {
        this.handleError(error, errorCallback);
    }
}

async createBuyer(buyerDetails, errorCallback) {
    try {
        const token = await this.getTokenOrThrow("Only authenticated users can create buyers.");

        const payload = {
            username: buyerDetails.username,
            email: buyerDetails.email,
            location: buyerDetails.location
        };

        const response = await this.axiosClient.post('/buyers', payload, {
            headers: {
                Authorization: `Bearer ${token}`
            }
        });

        return response.data;
    } catch (error) {
        this.handleError(error, errorCallback);
    }
}

async createGarage(garageDetails, errorCallback) {
    try {
        const token = await this.getTokenOrThrow("Only authenticated users can create garages.");

        const payload = {
            garageName: garageDetails.garageName,
            startDate: garageDetails.startDate,
            endDate: garageDetails.endDate,
            location: garageDetails.location,
            description: garageDetails.description
        };
 console.log("Sending payload to server:", payload);
        const response = await this.axiosClient.post('/garages', payload, {
            headers: {
                Authorization: `Bearer ${token}`
            }
        });
  console.log("Server response for create garage:", response.data);
        return response.data;
 } catch (error) {
        console.error("Error in createGarage:", error);  // Log the error
        this.handleError(error, errorCallback);
    }
}

    async getGaragesBySeller(sellerId) {
        try {
            const response = await this.axiosClient.get(`/sellers/${sellerId}/garages`);
            return response.data;
        } catch (error) {
            console.error("Error fetching garages:", error);
            throw error; // Rethrow the error for the caller to handle
        }
    }
async getRecentItems(lastEvaluatedKey, errorCallback) {
    try {
        // Prepare query parameters
        const queryParams = (typeof lastEvaluatedKey === 'string' && lastEvaluatedKey.length > 0) ? { next: lastEvaluatedKey } : {};
        console.log("[getRecentItems] Making API call to items/recent with params:", queryParams);

        // Make the GET request to the 'items/recent' endpoint
        const response = await this.axiosClient.get('items/recent', { params: queryParams });
        console.log("[getRecentItems] Response data:", response.data);

        // Destructure the necessary data from the response
        const { itemModels, lastEvaluatedKey: newLastEvaluatedKey } = response.data;

        console.log("[getRecentItems] Items received:", itemModels);
        console.log("[getRecentItems] New lastEvaluatedKey:", newLastEvaluatedKey);

        return {
            items: itemModels,
            lastEvaluatedKey: newLastEvaluatedKey
        };
    } catch (error) {
        console.error("[getRecentItems] Error:", error);
        if (errorCallback) {
            errorCallback(error);
        }
    }
}
    /**
     * Helper method to log the error and run any error functions.
     * @param error The error received from the server.
     * @param errorCallback (Optional) A function to execute if the call fails.
     */
    handleError(error, errorCallback) {
        console.error(error);
        const errorFromApi = error?.response?.data?.error_message;
        if (errorFromApi) {
            console.error(errorFromApi)
            error.message = errorFromApi;
        }
        if (errorCallback) {
            errorCallback(error);
        }
    }
    async updateSeller(sellerId, sellerDetails, errorCallback) {
        try {
            const token = await this.getTokenOrThrow("Only authenticated users can update seller details.");

            const payload = {
                username: sellerDetails.username,
                email: sellerDetails.email,
                location: sellerDetails.location,
                contactInfo: sellerDetails.contactInfo
            };

            const response = await this.axiosClient.put(`/sellers/${sellerId}`, payload, {
                headers: {
                    Authorization: `Bearer ${token}`
                }
            });

            return response.data;
        } catch (error) {
            this.handleError(error, errorCallback);
        }
    }

    async updateBuyer(buyerId, buyerDetails, errorCallback) {
        try {
            const token = await this.getTokenOrThrow("Only authenticated users can update buyer details.");

            const payload = {
                username: buyerDetails.username,
                email: buyerDetails.email,
                location: buyerDetails.location
            };

            const response = await this.axiosClient.put(`/buyers/${buyerId}`, payload, {
                    headers: {
                        Authorization: `Bearer ${token}`
                    }
                });

            return response.data;
        } catch (error) {
            this.handleError(error, errorCallback);
        }
    }

    async getItem(garageId, itemId, errorCallback) {
        try {
            const response = await this.axiosClient.get(`/items/${garageId}/${itemId}`);
            return response.data;
        } catch (error) {
            this.handleError(error, errorCallback);
        }
    }
}
