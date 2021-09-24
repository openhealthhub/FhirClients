import './style.css';
import {CLIENT_MAP} from "./client-map";

const headerDiv = document.createElement('div');
const preElem = document.createElement('pre');

function onTabClick(resourceOrAction, event) {
  preElem.innerHTML = 'Loading...';
  document.querySelectorAll('.active').forEach(elem => elem.className = undefined);
  event.target.className = 'active';
  preElem.className = undefined;
  const resourceWithAction = resourceOrAction.split('.');
  const resourceName = resourceWithAction[0];
  let client = CLIENT_MAP[resourceName];
  return client[resourceWithAction[1]]()
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
  createTab('Appointment.get');
  createTab('Appointment.create');
  createTab('Binary.create');
  createTab('CarePlan.get');
  createTab('CarePlan.create');
  createTab('PlanDefinition.get');
  createTab('PlanDefinition.search');
  createTab('Questionnaire.get');
  createTab('QuestionnaireResponse.get');
  createTab('QuestionnaireResponse.search');
  createTab('Subscription.create');
  createTab('VitalSigns.get');
  createTab('VitalSigns.search');
  document.body.append(headerDiv, preElem)
}

initPage();

