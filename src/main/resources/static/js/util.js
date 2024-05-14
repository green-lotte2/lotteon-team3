// GET
async function fetchGet(url){
    console.log("fetchGet : "+url);
    try{
        const response = await fetch(url);
        if(!response.ok){
            throw new Error("response not ok");
        }
        const data = await response.json();
        console.log("fetchGet data : "+data);
        return data;

    }catch (err) {
        console.log(err);
    }
}
// POST
async function fetchPost(url, jsonData) {
    console.log("fetchPost...1", jsonData);

    try {
        const response = await fetch(url, {
            method: 'POST',
            headers: {"Content-type":"application/json"},
            body: JSON.stringify(jsonData)
        });

        console.log("fetchPost...3", JSON.stringify(jsonData));

        if (!response.ok) {
            console.log("fetchPost...4");
            return null;
        }

        const data = await response.json();
        console.log("fetchData2...5 : ", data);

        return data;

    } catch (err) {
        console.log(err);
        return null;
    }
}


// PUT
async function fetchPut(url, jsonData){
    console.log("fetchPut...1");
    try{
        const response = await fetch(url, {
            method: 'PUT',
            headers: {"Content-type":"application/json"},
            body: JSON.stringify(jsonData)
        });
        console.log("fetchPut...2");
        if(!response.ok){
            throw new Error('response not ok');
        }
        console.log("fetchPut...3");
        const data = await response.json();
        console.log("data1 : " + data);
        console.log("fetchPut...4");
        return data;

    }catch (err) {
        console.log(err);
        return null;
    }
}
// DELETE
async function fetchDelete(url) {
    try {
        const response = await fetch(url, {
            method: 'DELETE'
        });
        console.log("fetchDelete : " + response);
        if (!response.ok) {
            throw new Error("response not ok");
        }
        const data = await response.json();
        console.log("fetchDelete data : " + data);

        return data;

    } catch (err) {
        console.log(err);
    }
}