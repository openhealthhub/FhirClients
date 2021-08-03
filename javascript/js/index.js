import './style.css';
import {CLIENT_MAP} from "./client-map";

const headerDiv = document.createElement('div');
const preElem = document.createElement('pre');

function onTabClick(resourceName, event) {
  document.querySelectorAll('.active').forEach(elem => elem.className = '');
  event.target.className = 'active';
  return CLIENT_MAP[resourceName].get().then(appointment => preElem.innerHTML = JSON.stringify(appointment, null, 4));
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
  document.body.append(headerDiv, preElem)
}

initPage();

