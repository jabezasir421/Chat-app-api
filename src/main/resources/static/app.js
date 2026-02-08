const chatLog = document.getElementById("chatLog");
const chatForm = document.getElementById("chatForm");
const chatInput = document.getElementById("chatInput");
const connectBtn = document.getElementById("connectBtn");
const userIdInput = document.getElementById("userId");
const taskForm = document.getElementById("taskForm");
const taskTitle = document.getElementById("taskTitle");
const taskDescription = document.getElementById("taskDescription");
const taskCategory = document.getElementById("taskCategory");
const taskStatus = document.getElementById("taskStatus");
const filterCategory = document.getElementById("filterCategory");
const refreshTasks = document.getElementById("refreshTasks");
const taskList = document.getElementById("taskList");

let stompClient = null;

const state = {
  userId: "",
};

function setStatus(message) {
  connectBtn.textContent = message;
  setTimeout(() => {
    connectBtn.textContent = "Connect";
  }, 1200);
}

function appendMessage(message) {
  const wrapper = document.createElement("div");
  wrapper.className = "chat-bubble";
  const meta = document.createElement("small");
  meta.textContent = `${message.senderId} · ${new Date(message.createdAt).toLocaleTimeString()}`;
  const content = document.createElement("div");
  content.textContent = message.content;
  wrapper.appendChild(meta);
  wrapper.appendChild(content);
  chatLog.prepend(wrapper);
}

async function loadRecentMessages() {
  const res = await fetch("/api/messages?limit=50");
  if (!res.ok) return;
  const messages = await res.json();
  chatLog.innerHTML = "";
  messages.forEach(appendMessage);
}

function connectSocket() {
  if (!state.userId) {
    setStatus("Set user ID");
    return;
  }
  const socket = new SockJS("/ws");
  stompClient = Stomp.over(socket);
  stompClient.connect({}, () => {
    stompClient.subscribe("/topic/messages", (msg) => {
      const payload = JSON.parse(msg.body);
      appendMessage(payload);
    });
    setStatus("Connected");
  });
}

chatForm.addEventListener("submit", (event) => {
  event.preventDefault();
  if (!stompClient || !stompClient.connected) {
    setStatus("Not connected");
    return;
  }
  const content = chatInput.value.trim();
  if (!content) return;
  const payload = {
    senderId: state.userId,
    content,
  };
  stompClient.send("/app/chat.send", {}, JSON.stringify(payload));
  chatInput.value = "";
});

connectBtn.addEventListener("click", () => {
  state.userId = userIdInput.value.trim();
  connectSocket();
});

async function fetchTasks() {
  if (!state.userId) return;
  const params = new URLSearchParams();
  if (taskStatus.value) params.append("status", taskStatus.value);
  if (filterCategory.value.trim()) params.append("category", filterCategory.value.trim());
  const res = await fetch(`/api/tasks?${params.toString()}`, {
    headers: {
      "X-User-Id": state.userId,
    },
  });
  if (!res.ok) return;
  const tasks = await res.json();
  taskList.innerHTML = "";
  tasks.forEach(renderTask);
}

function renderTask(task) {
  const card = document.createElement("div");
  card.className = "task-item";

  const header = document.createElement("header");
  const title = document.createElement("strong");
  title.textContent = task.title;
  const status = document.createElement("span");
  status.className = "task-meta";
  status.textContent = task.status;
  header.appendChild(title);
  header.appendChild(status);

  const meta = document.createElement("div");
  meta.className = "task-meta";
  meta.textContent = task.category ? `Category: ${task.category}` : "No category";

  const body = document.createElement("div");
  body.textContent = task.description || "No description";

  const actions = document.createElement("div");
  actions.className = "task-actions";

  const toggleBtn = document.createElement("button");
  toggleBtn.textContent = task.status === "PENDING" ? "Mark actioned" : "Mark pending";
  toggleBtn.addEventListener("click", async () => {
    await updateTask(task.id, {
      status: task.status === "PENDING" ? "ACTIONED" : "PENDING",
    });
    fetchTasks();
  });

  const deleteBtn = document.createElement("button");
  deleteBtn.textContent = "Delete";
  deleteBtn.addEventListener("click", async () => {
    await deleteTask(task.id);
    fetchTasks();
  });

  actions.appendChild(toggleBtn);
  actions.appendChild(deleteBtn);

  card.appendChild(header);
  card.appendChild(meta);
  card.appendChild(body);
  card.appendChild(actions);

  taskList.appendChild(card);
}

async function createTask(payload) {
  const res = await fetch("/api/tasks", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      "X-User-Id": state.userId,
    },
    body: JSON.stringify(payload),
  });
  return res.ok;
}

async function updateTask(id, payload) {
  await fetch(`/api/tasks/${id}`, {
    method: "PUT",
    headers: {
      "Content-Type": "application/json",
      "X-User-Id": state.userId,
    },
    body: JSON.stringify(payload),
  });
}

async function deleteTask(id) {
  await fetch(`/api/tasks/${id}`, {
    method: "DELETE",
    headers: {
      "X-User-Id": state.userId,
    },
  });
}

taskForm.addEventListener("submit", async (event) => {
  event.preventDefault();
  if (!state.userId) {
    setStatus("Set user ID");
    return;
  }
  const payload = {
    title: taskTitle.value.trim(),
    description: taskDescription.value.trim() || null,
    category: taskCategory.value.trim() || null,
  };
  const ok = await createTask(payload);
  if (ok) {
    taskTitle.value = "";
    taskDescription.value = "";
    taskCategory.value = "";
    fetchTasks();
  }
});

refreshTasks.addEventListener("click", (event) => {
  event.preventDefault();
  fetchTasks();
});

document.addEventListener("DOMContentLoaded", () => {
  loadRecentMessages();
});