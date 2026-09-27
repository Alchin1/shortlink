const form = document.querySelector('#shorten-form');
const urlInput = document.querySelector('#url');
const expiresInput = document.querySelector('#expires');
const submitButton = document.querySelector('#submit');
const result = document.querySelector('#result');
const error = document.querySelector('#error');
const shortUrl = document.querySelector('#short-url');
const code = document.querySelector('#code');
const copyButton = document.querySelector('#copy');
const statsButton = document.querySelector('#stats');
const statsBox = document.querySelector('#stats-box');

let currentCode = null;

form.addEventListener('submit', async (event) => {
  event.preventDefault();
  error.hidden = true;
  result.hidden = true;
  statsBox.hidden = true;
  submitButton.disabled = true;
  submitButton.textContent = 'Working...';

  const payload = { url: urlInput.value.trim() };
  if (expiresInput.value) payload.expiresAt = expiresInput.value;

  try {
    const response = await fetch('/api/links', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(payload)
    });
    const data = await response.json();
    if (!response.ok) throw new Error(data.message || 'Could not create the short link.');

    currentCode = data.code;
    shortUrl.href = data.shortUrl;
    shortUrl.textContent = data.shortUrl;
    code.textContent = data.code;
    result.hidden = false;
  } catch (err) {
    error.textContent = err.message;
    error.hidden = false;
  } finally {
    submitButton.disabled = false;
    submitButton.textContent = 'Shorten';
  }
});

copyButton.addEventListener('click', async () => {
  await navigator.clipboard.writeText(shortUrl.textContent);
  copyButton.textContent = 'Copied';
  setTimeout(() => copyButton.textContent = 'Copy', 1300);
});

statsButton.addEventListener('click', async () => {
  if (!currentCode) return;
  try {
    const response = await fetch(`/api/links/${currentCode}`);
    const data = await response.json();
    if (!response.ok) throw new Error(data.message || 'Could not load stats.');
    document.querySelector('#clicks').textContent = data.clicks;
    document.querySelector('#created').textContent = formatDate(data.createdAt);
    document.querySelector('#expiration').textContent = data.expiresAt ? formatDate(data.expiresAt) : 'Never';
    statsBox.hidden = false;
  } catch (err) {
    error.textContent = err.message;
    error.hidden = false;
  }
});

function formatDate(value) {
  return new Date(value).toLocaleString([], { dateStyle: 'medium', timeStyle: 'short' });
}
