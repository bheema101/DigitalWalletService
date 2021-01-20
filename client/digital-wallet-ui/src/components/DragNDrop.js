import React, { Component } from "react";
import { FileDrop } from "react-file-drop";
import axios from "axios";
import "./Demo.css";

class DragNDrop extends Component {
  state = {
    id: "",
    files: [],
  };
  base_url = "https://localhost/dws";
  componentDidMount() {
    //getAllFiles
    console.log("Mounted");
    axios({
      method: "get",
      url: `${this.base_url}/getAllFiles/PN101/TU102`,
    })
      .then((response) => {
        const data = response.data[0]
        console.log(response);
        this.setState({ files: data.fileNames, id: data.id });
      })
      .catch((err) => console.log(err));
  }

  onDropFile = (files, event) => {
    console.log(files[0]);
    const formdata = new FormData();
    formdata.append("file", files[0]);
    formdata.append("tuid", "TU102");
    formdata.append("tripid", "TRIP101");
    formdata.append("pnr", "PN101");
    axios({
      method: "post",
      url: `${this.base_url}/uploadfilewithdata`,
      data: formdata,
      headers: { "Content-Type": "multipart/form-data" },
    })
      .then(() => {
        console.log("File uploaded successfully");
        axios({
          method: "get",
          url: `${this.base_url}/getAllFiles/PN101/TU102`,
        })
          .then((response) => {
            this.setState({ files: [...response.data[0].fileNames] });
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

            {this.state.files.length > 0
              ? this.state.files.map((file) => (
                  <a
                    href={`${this.base_url}/download?fileId=${this.state.id}&fileName=${file}`}
                    key={file}
                  >
                    {file}
                    <br />
                  </a>
                ))
              : `Drop some files here!`}
          </FileDrop>
        </div>
      </div>
    );
  }
}

export default DragNDrop;
