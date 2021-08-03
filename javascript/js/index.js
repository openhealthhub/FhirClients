import './style.css';
import {CLIENT_MAP} from "./client-map";

const headerDiv = document.createElement('div');
const preElem = document.createElement('pre');

function onTabClick(resourceName, event) {
  document.querySelectorAll('.active').forEach(elem => elem.className = undefined);
  event.target.className = 'active';
  preElem.className = undefined;
  return CLIENT_MAP[resourceName].get()
    .then(resource => preElem.innerHTML = JSON.stringify(resource, null, 4))
    .catch(error => {
      preElem.innerHTML = error.message;
      preElem.className = 'error';
    });
}

function createTab(resourceName) {
  let tabElem = document.createElement('div');
  tabElem.innerHTML = resourceName;
  tabElem.addEventListener('click',
    event => onTabClick(resourceName, event));
  headerDiv.appendChild(tabElem);
}

function initPage() {
  headerDiv.className = 'header';
  createTab('Appointment');
  createTab('Questionnaire');
  createTab('Subscription');
  document.body.append(headerDiv, preElem)
}

initPage();

