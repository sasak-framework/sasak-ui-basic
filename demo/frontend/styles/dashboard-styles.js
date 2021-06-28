import '@vaadin/vaadin-lumo-styles/all-imports';

const $_documentContainer = document.createElement('template');

$_documentContainer.innerHTML = `
<custom-style>
    <style>
        .chart-container {
            margin: auto;
        }

        .chart-container: {
            background-color: aliceblue;
            border: 1px solid blue;
        }
    </style>
    <dom-module id="color-selector" theme-for="vaadin-combo-box">
            <template>
                <style>
                    :host(.user-id) [part="input"] [slot="value"] {
                        background-color: transparent;
                        border-bottom: 1px solid lightslategray;
                        border-radius: unset;
                        color: white;
                    }
                    :host([focus-ring]) [part="input-field"] {
                        box-shadow: none;
                    }
                </style>
            </template>
        </dom-module>
</custom-style>

`;

document.head.appendChild($_documentContainer.content);