import Cake from './Cake';
import React, {Component} from 'react';

class CakeList extends React.Component {

    constructor(props) {
        super(props)
        
        this.state = {
            cakes: []
        }
    }

    componentDidMount() {
        fetch('/cakes').then(cakeResponse => {
          return cakeResponse.json()
        }).then(cakes => {
          
          this.setState({
            cakes
          })
        })
      }

    render() {
        return (
          <div className="m-8 grid grid-cols-3 gap-8 ">
            {this.state.cakes.map(cake => (
                <div className="card">
                    <Cake description={cake.desc} image={cake.image} title={cake.title}/>
                </div>
                )
            )}
          </div>
        


        )

    }

}

export default CakeList;
