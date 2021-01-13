import React, { Component } from "react";
import { FileDrop } from "react-file-drop";
import axios from "axios";
import "./Demo.css";

class DragNDrop extends Component {
  state = {
    files: []
  };
  ngrok_url = "http://3bd881ab1550.ngrok.io";
  componentDidMount() {
    //getAllFiles
    console.log("Mounted");
    axios({
      method: "get",
      url: `${this.ngrok_url}/getAllFiles`,
    })
      .then((response) => {
        this.setState({files: [...response.data]});
      })
      .catch((err) => console.log(err));
  }

  onDropFile = (files, event) => {
    console.log(files[0]);
    const formdata = new FormData();
    formdata.append("file", files[0]);
    formdata.append("tuid", "ABCD");
    formdata.append("tripid", "1234");
    formdata.append("pnr", "123-4567890");
    axios({
      method: "post",
      url: `${this.ngrok_url}/uploadfilewithdata`,
      data: formdata,
      headers: { "Content-Type": "multipart/form-data" },
    })
      .then(() => {
        console.log("File uploaded successfully");
        axios({
          method: "get",
          url: `${this.ngrok_url}/getAllFiles`,
        })
          .then((response) => {
            this.setState({files : [...response.data]});
          })
          .catch((err) => console.log(err));
        
      })
      .catch((err) => console.log(err));
  };

  render() {
    return (
      <div>
        <h1>Digital Wallet Demo</h1>
        <div>
          <FileDrop onDrop={(files, event) => this.onDropFile(files, event)}>
            {this.state.files.length > 0 ? (
              this.state.files.map((file) => (
                <a
                  href={`${this.ngrok_url}/fileResource?fileName=${file}`}
                  key={file}
                >
                  {file}
                  <br/>
                </a>
              ))
            ) : (
              `Drop some files here!`
            )}
          </FileDrop>
        </div>
      </div>
    );
  }
}

export default DragNDrop;
