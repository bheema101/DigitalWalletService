import React, { Component } from "react";
import { FileDrop } from "react-file-drop";
import axios from "axios";
import "./Demo.css";

class DragNDrop extends Component {
  state = {
    files: [],
  };
  componentDidMount() {
    //getAllFiles
    console.log("Mounted");
    axios({
      method: "get",
      url: "http://ff038484f781.ngrok.io/getAllFiles",
    })
      .then((response) => {
        this.setState((this.state.files = [...response.data]));
      })
      .catch((err) => console.log(err));
  }
  onDropFile = (files, event) => {
    console.log(files[0]);
    const formdata = new FormData();
    formdata.append("file", files[0]);
    axios({
      method: "post",
      url: "http://ff038484f781.ngrok.io/upload",
      data: formdata,
      headers: { "Content-Type": "multipart/form-data" },
    })
      .then(() => {
        console.log("File uploaded successfully");
        axios({
          method: "get",
          url: "http://ff038484f781.ngrok.io/getAllFiles",
        })
          .then((response) => {
            this.setState((this.state.files = [...response.data]));
          })
          .catch((err) => console.log(err));
      })
      .catch((err) => console.log(err));
  };

  onDownload = (file) => {
    console.log(file);
    axios({
      method: "post",
      url: `http://ff038484f781.ngrok.io/fileResource?fileName=${file}`,
    })
      .then(() => console.log("File downloaded"))
      .catch((err) => console.log(err));
  };

  render() {
    return (
      <div>
        <h1>Digital Wallet Demo</h1>
        <div>
          <FileDrop
            onDrop={(files, event) => this.onDropFile(files, event)}
          >
            {this.state.files.length > 0
              ? this.state.files.map((file) => (
                  <a href={`http://ff038484f781.ngrok.io/fileResource?fileName=${file}` } key={file}>{file}</a>
                ))
              : `Drop some files here!`}
          </FileDrop>
        </div>
      </div>
    );
  }
}

export default DragNDrop;
