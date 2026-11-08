// ===== API Base URL =====
const API = '';

// ===== Cache for lookups =====
let vehicleTypesCache = [];
let locationsCache = [];
let currentModalType = '';

// ===== Initialize =====
document.addEventListener('DOMContentLoaded', () => {
    setupNavigation();
    setupMobileMenu();
    loadDashboard();
});

// ===== Navigation =====
function setupNavigation() {
    document.querySelectorAll('.nav-item').forEach(item => {
        item.addEventListener('click', (e) => {
            e.preventDefault();
            const section = item.dataset.section;
            navigateTo(section);
        });
    });

    document.querySelectorAll('.card-link').forEach(link => {
        link.addEventListener('click', (e) => {
            e.preventDefault();
            navigateTo(link.dataset.goto);
        });
    });
}

function navigateTo(section) {
    // Update nav
    document.querySelectorAll('.nav-item').forEach(n => n.classList.remove('active'));
    document.querySelector(`[data-section="${section}"]`).classList.add('active');

    // Update sections
    document.querySelectorAll('.content-section').forEach(s => s.classList.remove('active'));
    const sectionEl = document.getElementById(`section-${section}`);
    sectionEl.classList.remove('active');
    // Force reflow for animation
    void sectionEl.offsetWidth;
    sectionEl.classList.add('active');

    // Update title
    const titles = {
        'dashboard': 'Dashboard',
        'vehicles': 'Vehicles',
        'clients': 'Clients',
        'reservations': 'Reservations',
        'vehicle-types': 'Vehicle Types',
        'locations': 'Locations',
        'administrators': 'Administrators',
        'accounts': 'Accounts'
    };
    document.getElementById('pageTitle').textContent = titles[section] || section;

    // Load data
    loadSectionData(section);

    // Close mobile sidebar
    document.getElementById('sidebar').classList.remove('open');
}

function setupMobileMenu() {
    document.getElementById('menuToggle').addEventListener('click', () => {
        document.getElementById('sidebar').classList.toggle('open');
    });
}

// ===== Data Loading =====
async function loadSectionData(section) {
    switch (section) {
        case 'dashboard': loadDashboard(); break;
        case 'vehicles': loadVehicles(); break;
        case 'clients': loadClients(); break;
        case 'reservations': loadReservations(); break;
        case 'vehicle-types': loadVehicleTypes(); break;
        case 'locations': loadLocations(); break;
        case 'administrators': loadAdministrators(); break;
        case 'accounts': loadAccounts(); break;
    }
}

async function fetchData(endpoint) {
    try {
        const res = await fetch(`${API}/${endpoint}`);
        return await res.json();
    } catch (err) {
        showToast('Failed to fetch data: ' + err.message, 'error');
        return [];
    }
}

// ===== Dashboard =====
async function loadDashboard() {
    const [vehicles, clients, reservations, locations, types] = await Promise.all([
        fetchData('vehicles'),
        fetchData('clients'),
        fetchData('reservations'),
        fetchData('locations'),
        fetchData('vehicle-types')
    ]);

    // Cache for lookups
    vehicleTypesCache = types;
    locationsCache = locations;

    // Update stats with animation
    animateNumber('stat-vehicles', vehicles.length);
    animateNumber('stat-clients', clients.length);
    animateNumber('stat-reservations', reservations.length);
    animateNumber('stat-locations', locations.length);

    // Recent vehicles
    const vehiclesHtml = vehicles.slice(0, 4).map(v => {
        const typeName = types.find(t => t.id === v.vehicleTypeId);
        return `<div class="dash-item">
            <div class="dash-item-left">
                <span class="dash-item-title">${v.brand}</span>
                <span class="dash-item-sub">${v.power} · ${v.numberOfSeats} seats</span>
            </div>
            <span class="badge">${typeName ? typeName.name : 'N/A'}</span>
        </div>`;
    }).join('');
    document.getElementById('dashboard-vehicles').innerHTML = vehiclesHtml || emptyState('No vehicles');

    // Recent reservations
    const resHtml = reservations.slice(0, 4).map(r => `
        <div class="dash-item">
            <div class="dash-item-left">
                <span class="dash-item-title">${r.destination}</span>
                <span class="dash-item-sub">${formatDate(r.arrivalDate)}</span>
            </div>
            <span class="status-badge status-${r.status}">${r.status}</span>
        </div>
    `).join('');
    document.getElementById('dashboard-reservations').innerHTML = resHtml || emptyState('No reservations');
}

function animateNumber(id, target) {
    const el = document.getElementById(id);
    let current = 0;
    const step = Math.max(1, Math.floor(target / 20));
    const interval = setInterval(() => {
        current += step;
        if (current >= target) {
            current = target;
            clearInterval(interval);
        }
        el.textContent = current;
    }, 30);
}

// ===== Vehicles =====
async function loadVehicles() {
    const [vehicles, types, locs] = await Promise.all([
        fetchData('vehicles'),
        fetchData('vehicle-types'),
        fetchData('locations')
    ]);
    vehicleTypesCache = types;
    locationsCache = locs;

    const html = vehicles.map(v => {
        const typeName = types.find(t => t.id === v.vehicleTypeId);
        const locName = locs.find(l => l.id === v.locationId);
        return `<tr>
            <td><strong>${v.brand}</strong></td>
            <td>${v.power}</td>
            <td>${v.numberOfSeats}</td>
            <td><span class="badge">${typeName ? typeName.name : 'N/A'}</span></td>
            <td>${locName ? locName.name : 'N/A'}</td>
            <td><button class="btn btn-sm btn-danger" onclick="deleteEntity('vehicles', '${v.id}')">Delete</button></td>
        </tr>`;
    }).join('');
    document.getElementById('vehicles-table').innerHTML = html || '<tr><td colspan="6">' + emptyState('No vehicles found') + '</td></tr>';
}

// ===== Clients =====
async function loadClients() {
    const clients = await fetchData('clients');
    const html = clients.map(c => `<tr>
        <td><strong>${c.name}</strong></td>
        <td>${c.address}</td>
        <td>${c.phone}</td>
        <td>${c.email}</td>
        <td><span class="badge">${c.reservationIds ? c.reservationIds.length : 0}</span></td>
        <td><button class="btn btn-sm btn-danger" onclick="deleteEntity('clients', '${c.id}')">Delete</button></td>
    </tr>`).join('');
    document.getElementById('clients-table').innerHTML = html || '<tr><td colspan="6">' + emptyState('No clients found') + '</td></tr>';
}

// ===== Reservations =====
let allReservations = [];

async function loadReservations(statusFilter) {
    if (!statusFilter || statusFilter === 'all') {
        allReservations = await fetchData('reservations');
    }

    const filtered = statusFilter && statusFilter !== 'all'
        ? allReservations.filter(r => r.status === statusFilter)
        : allReservations;

    const html = filtered.map(r => `<tr>
        <td><strong>${r.destination}</strong></td>
        <td>${formatDate(r.arrivalDate)}</td>
        <td><span class="status-badge status-${r.status}">${r.status}</span></td>
        <td>${r.numberOfTravelers}</td>
        <td>${r.comment || '-'}</td>
        <td><button class="btn btn-sm btn-danger" onclick="deleteEntity('reservations', '${r.id}')">Delete</button></td>
    </tr>`).join('');
    document.getElementById('reservations-table').innerHTML = html || '<tr><td colspan="6">' + emptyState('No reservations found') + '</td></tr>';
}

function filterReservations(status, btn) {
    document.querySelectorAll('.filter-btn').forEach(b => b.classList.remove('active'));
    btn.classList.add('active');
    loadReservations(status);
}

// ===== Vehicle Types =====
async function loadVehicleTypes() {
    const types = await fetchData('vehicle-types');
    vehicleTypesCache = types;

    const html = types.map(t => `
        <div class="entity-card">
            <div class="entity-card-title">${t.name}</div>
            <div class="entity-card-info">Rate: $${t.rate.toFixed(2)} / day</div>
            <div class="entity-card-actions">
                <button class="btn btn-sm btn-danger" onclick="deleteEntity('vehicle-types', '${t.id}')">Delete</button>
            </div>
        </div>
    `).join('');
    document.getElementById('vehicle-types-cards').innerHTML = html || emptyState('No vehicle types');
}

// ===== Locations =====
async function loadLocations() {
    const locations = await fetchData('locations');
    locationsCache = locations;

    const html = locations.map(l => `
        <div class="entity-card">
            <div class="entity-card-title">📍 ${l.name}</div>
            <div class="entity-card-info">ID: ${l.id.substring(0, 8)}...</div>
            <div class="entity-card-actions">
                <button class="btn btn-sm btn-danger" onclick="deleteEntity('locations', '${l.id}')">Delete</button>
            </div>
        </div>
    `).join('');
    document.getElementById('locations-cards').innerHTML = html || emptyState('No locations');
}

// ===== Administrators =====
async function loadAdministrators() {
    const admins = await fetchData('administrators');
    const html = admins.map(a => `<tr>
        <td><strong>${a.name}</strong></td>
        <td>${a.email}</td>
        <td>${a.address}</td>
        <td><span class="badge">${a.type}</span></td>
        <td><button class="btn btn-sm btn-danger" onclick="deleteEntity('administrators', '${a.id}')">Delete</button></td>
    </tr>`).join('');
    document.getElementById('administrators-table').innerHTML = html || '<tr><td colspan="5">' + emptyState('No administrators') + '</td></tr>';
}

// ===== Accounts =====
async function loadAccounts() {
    const accounts = await fetchData('accounts');
    const html = accounts.map(a => `<tr>
        <td><strong>${a.login}</strong></td>
        <td>${'•'.repeat(a.password.length)}</td>
        <td><button class="btn btn-sm btn-danger" onclick="deleteEntity('accounts', '${a.id}')">Delete</button></td>
    </tr>`).join('');
    document.getElementById('accounts-table').innerHTML = html || '<tr><td colspan="3">' + emptyState('No accounts') + '</td></tr>';
}

// ===== Delete Entity =====
async function deleteEntity(endpoint, id) {
    if (!confirm('Are you sure you want to delete this item?')) return;

    try {
        const res = await fetch(`${API}/${endpoint}/${id}`, { method: 'DELETE' });
        if (res.ok) {
            showToast('Deleted successfully!', 'success');
            // Reload the current section
            const activeNav = document.querySelector('.nav-item.active');
            loadSectionData(activeNav.dataset.section);
        } else {
            showToast('Failed to delete', 'error');
        }
    } catch (err) {
        showToast('Error: ' + err.message, 'error');
    }
}

// ===== Modal =====
function openModal(type) {
    currentModalType = type;
    const overlay = document.getElementById('modalOverlay');
    const title = document.getElementById('modalTitle');
    const body = document.getElementById('modalBody');

    const forms = {
        vehicle: {
            title: 'Add New Vehicle',
            fields: `
                <div class="form-group">
                    <label>Brand</label>
                    <input type="text" name="brand" placeholder="e.g. Toyota Camry" required>
                </div>
                <div class="form-group">
                    <label>Power</label>
                    <input type="text" name="power" placeholder="e.g. 200HP" required>
                </div>
                <div class="form-group">
                    <label>Number of Seats</label>
                    <input type="number" name="numberOfSeats" placeholder="e.g. 5" min="1" required>
                </div>
                <div class="form-group">
                    <label>Vehicle Type</label>
                    <select name="vehicleTypeId" required>
                        <option value="">Select type...</option>
                        ${vehicleTypesCache.map(t => `<option value="${t.id}">${t.name} ($${t.rate}/day)</option>`).join('')}
                    </select>
                </div>
                <div class="form-group">
                    <label>Location</label>
                    <select name="locationId" required>
                        <option value="">Select location...</option>
                        ${locationsCache.map(l => `<option value="${l.id}">${l.name}</option>`).join('')}
                    </select>
                </div>`
        },
        client: {
            title: 'Add New Client',
            fields: `
                <div class="form-group">
                    <label>Name</label>
                    <input type="text" name="name" placeholder="Full name" required>
                </div>
                <div class="form-group">
                    <label>Address</label>
                    <input type="text" name="address" placeholder="Address" required>
                </div>
                <div class="form-group">
                    <label>Phone</label>
                    <input type="text" name="phone" placeholder="Phone number" required>
                </div>
                <div class="form-group">
                    <label>Email</label>
                    <input type="email" name="email" placeholder="Email address" required>
                </div>`
        },
        reservation: {
            title: 'Add New Reservation',
            fields: `
                <div class="form-group">
                    <label>Destination</label>
                    <input type="text" name="destination" placeholder="e.g. New York" required>
                </div>
                <div class="form-group">
                    <label>Arrival Date</label>
                    <input type="date" name="arrivalDate" required>
                </div>
                <div class="form-group">
                    <label>Status</label>
                    <select name="status" required>
                        <option value="pending">Pending</option>
                        <option value="accepted">Accepted</option>
                        <option value="refused">Refused</option>
                    </select>
                </div>
                <div class="form-group">
                    <label>Number of Travelers</label>
                    <input type="number" name="numberOfTravelers" min="1" placeholder="e.g. 3" required>
                </div>
                <div class="form-group">
                    <label>Comment</label>
                    <input type="text" name="comment" placeholder="Optional comment">
                </div>`
        },
        vehicleType: {
            title: 'Add Vehicle Type',
            fields: `
                <div class="form-group">
                    <label>Name</label>
                    <input type="text" name="name" placeholder="e.g. SUV" required>
                </div>
                <div class="form-group">
                    <label>Rate ($/day)</label>
                    <input type="number" name="rate" step="0.01" placeholder="e.g. 75.00" required>
                </div>`
        },
        location: {
            title: 'Add Location',
            fields: `
                <div class="form-group">
                    <label>Name</label>
                    <input type="text" name="name" placeholder="e.g. Airport Branch" required>
                </div>`
        },
        administrator: {
            title: 'Add Administrator',
            fields: `
                <div class="form-group">
                    <label>Name</label>
                    <input type="text" name="name" placeholder="Full name" required>
                </div>
                <div class="form-group">
                    <label>Email</label>
                    <input type="email" name="email" placeholder="Email" required>
                </div>
                <div class="form-group">
                    <label>Address</label>
                    <input type="text" name="address" placeholder="Address" required>
                </div>
                <div class="form-group">
                    <label>Role</label>
                    <select name="type" required>
                        <option value="Staff">Staff</option>
                        <option value="Manager">Manager</option>
                        <option value="SuperAdmin">SuperAdmin</option>
                    </select>
                </div>`
        },
        account: {
            title: 'Add Account',
            fields: `
                <div class="form-group">
                    <label>Login</label>
                    <input type="text" name="login" placeholder="Username" required>
                </div>
                <div class="form-group">
                    <label>Password</label>
                    <input type="password" name="password" placeholder="Password" required>
                </div>`
        }
    };

    const form = forms[type];
    if (!form) return;

    title.textContent = form.title;
    body.innerHTML = form.fields;
    overlay.classList.add('active');
}

function closeModal() {
    document.getElementById('modalOverlay').classList.remove('active');
    document.getElementById('modalForm').reset();
}

async function handleFormSubmit(e) {
    e.preventDefault();
    const formData = new FormData(e.target);
    const data = {};
    formData.forEach((value, key) => {
        // Convert numeric fields
        if (['numberOfSeats', 'numberOfTravelers'].includes(key)) {
            data[key] = parseInt(value);
        } else if (key === 'rate') {
            data[key] = parseFloat(value);
        } else if (key === 'arrivalDate') {
            data[key] = new Date(value).toISOString();
            data['arrivalTime'] = data[key];
        } else {
            data[key] = value;
        }
    });

    // Add empty arrays for list fields
    if (currentModalType === 'client') data.reservationIds = [];
    if (currentModalType === 'reservation') data.vehicleIds = [];

    const endpoints = {
        vehicle: 'vehicles',
        client: 'clients',
        reservation: 'reservations',
        vehicleType: 'vehicle-types',
        location: 'locations',
        administrator: 'administrators',
        account: 'accounts'
    };

    try {
        const res = await fetch(`${API}/${endpoints[currentModalType]}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(data)
        });

        if (res.ok) {
            showToast('Created successfully!', 'success');
            closeModal();
            // Reload section
            const sectionMap = {
                vehicle: 'vehicles',
                client: 'clients',
                reservation: 'reservations',
                vehicleType: 'vehicle-types',
                location: 'locations',
                administrator: 'administrators',
                account: 'accounts'
            };
            loadSectionData(sectionMap[currentModalType]);
        } else {
            const errText = await res.text();
            showToast('Error: ' + errText, 'error');
        }
    } catch (err) {
        showToast('Error: ' + err.message, 'error');
    }
}

// ===== Toast =====
function showToast(message, type) {
    const container = document.getElementById('toastContainer');
    const icons = { success: '✓', error: '✕' };
    const toast = document.createElement('div');
    toast.className = `toast toast-${type}`;
    toast.innerHTML = `<span>${icons[type] || ''}</span> ${message}`;
    container.appendChild(toast);
    setTimeout(() => {
        toast.style.opacity = '0';
        toast.style.transform = 'translateX(20px)';
        setTimeout(() => toast.remove(), 300);
    }, 3000);
}

// ===== Helpers =====
function formatDate(dateStr) {
    if (!dateStr) return 'N/A';
    const d = new Date(dateStr);
    return d.toLocaleDateString('en-US', { year: 'numeric', month: 'short', day: 'numeric' });
}

function emptyState(msg) {
    return `<div class="empty-state">
        <div class="empty-state-icon">📭</div>
        <p>${msg}</p>
    </div>`;
}

// Close modal on overlay click
document.getElementById('modalOverlay').addEventListener('click', (e) => {
    if (e.target === e.currentTarget) closeModal();
});

// Close modal on Escape key
document.addEventListener('keydown', (e) => {
    if (e.key === 'Escape') closeModal();
});
