package com.demazetech.coffee.api.objects;

public class GlobalEnums {
    
    public enum ProviderType {

        FACEBOOK("facebook"), GOOGLE("google");

        private String label;

        ProviderType (String label) {
            this.label = label;
        }

        public String getLabel() {
            return this.label;
        }
    }
}
