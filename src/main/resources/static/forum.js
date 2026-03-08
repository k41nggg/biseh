async function submitComment(event, postId) {
    event.preventDefault();
    const usernameEl = document.getElementById('username');
    const username = usernameEl ? usernameEl.value : undefined;
    const content = document.getElementById('content').value;
    const body = username ? {username, content} : {content};
    const res = await fetch('/api/posts/' + postId + '/comments', {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        credentials: 'same-origin',
        body: JSON.stringify(body)
    });
    if (res.ok) {
        const c = await res.json();
        const ul = document.getElementById('comments');
        const li = document.createElement('li');
        li.innerHTML = '<b>' + c.author.displayName + '</b>: ' + c.content;
        ul.appendChild(li);
        const contentEl = document.getElementById('content');
        if (contentEl) contentEl.value = '';
    } else {
        const txt = await res.text();
        alert('提交失败: ' + (txt || res.status));
    }
}

async function fetchSummary(postId) {
    const res = await fetch('/api/posts/' + postId + '/summary', {credentials: 'same-origin'});
    if (res.ok) {
        const text = await res.text();
        document.getElementById('summary').innerText = text;
    } else {
        document.getElementById('summary').innerText = '无法获取摘要';
    }
}

async function submitPost(e) {
    if (e) e.preventDefault();
    const usernameEl = document.getElementById('username');
    const data = {
        username: usernameEl ? usernameEl.value : undefined,
        title: document.getElementById('title').value,
        content: document.getElementById('content').value
    };
    if (data.username === undefined) delete data.username;
    const res = await fetch('/api/posts', {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        credentials: 'same-origin',
        body: JSON.stringify(data)
    });
    if (!res.ok) {
        const txt = await res.text();
        alert('发布失败: ' + (txt || res.status));
        return;
    }
    const p = await res.json();
    window.location = '/forum/posts/' + p.id;
}
