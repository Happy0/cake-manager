import React, {Component} from 'react';

class Cake extends React.Component {

    render() {
        const defaultImageUrl= "https://www.eglsf.info/wp-content/uploads/image-missing.png";
        return (<div className="bg-green-200 rounded-lg container p-1 center m-1 md:w-auto flex flex-col flex-wrap content-center ">
                    <div>
                        <img
                            className="max-w-full p-2"
                            height="250"
                            width="250"
                            src={this.props.image}
                            onError={(e)=>{e.target.onerror = null; e.target.src=defaultImageUrl } } />
                    </div>

                    <div className="text-center font-semibold p-1 text-lg">{this.props.title}</div>
                    <div className="text-center text-sm p-1" >{this.props.description}</div>
                </div>)
      }
}

export default Cake;
