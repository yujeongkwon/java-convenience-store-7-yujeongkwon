package store;

import store.controller.StoreController;

public class Application {
    public static void main(String[] args) {
        final Configuration configuration = new Configuration();
        StoreController storeController = configuration.storeController();
        storeController.run();
    }
}
