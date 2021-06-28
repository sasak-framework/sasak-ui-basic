// eagerly import theme styles so as we can override them
import '@vaadin/vaadin-lumo-styles/all-imports';

const $_documentContainer = document.createElement('template');

$_documentContainer.innerHTML = `
<custom-style>
    <style>
      .login-container {
          max-width: calc(100% - 20px);
          width: 320px;
          background-color: rgb(45, 68, 67);
          padding: 20px;
          border-radius: 4px;
          margin: auto;
          color: #fff;
          box-shadow: 3px 3px 4px rgba(0,0,0,0.2);
          height: 400px;
          max-height: calc(100% - 30px);
      }

      .login-logo {
        text-align: center;
        padding: 15px 0 20px;
        font-size: 100px;
        color: white;
      }

      .login-form {
        background: none;
        border: none;
        border-bottom: 1px solid #434a52;
        border-radius: 0;
        box-shadow: none;
        outline: none;
        color: inherit;
      }

      .login-button {
          height: 45px;
          color: white;
          border: none;
          border-radius: 4px;
          box-shadow: none;
          text-shadow: none;
          outline: none;
      }

      .login-button:hover, .login-green .login-button:active {
        background: #214a80;
        outline: none;
      }

      .login-button:active {
        transform: translateY(1px);
      }
    </style>
    <dom-module id="user-id" theme-for="vaadin-text-field">
        <template>
            <style>
                :host(.user-id) [part="input-field"] {
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
    <dom-module id="password" theme-for="vaadin-password-field">
        <template>
            <style>
                :host(.password) [part="input-field"] {
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
