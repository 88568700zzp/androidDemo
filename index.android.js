'use strict';
import React from 'react';
import {
    AppRegistry,
    StyleSheet,
    Text,
    View,
    Button,
    TextInput
} from 'react-native';

class HelloWorld extends React.Component {
    render() {
        return (
            <View style={styles.container}>
                <Text style={styles.hello}>Hello, World</Text>
                <Text style={styles.hello}>66666fdsa</Text>
                <Button
                  title="Learn More"
                  color="#841584"
                  accessibilityLabel="Learn more about this purple button"
                />
                <TextInput
                      defaultValue="{40}"
                    />
            </View>
    )
    }
}
var styles = StyleSheet.create({
    container: {
        flex: 1,
        justifyContent: 'center',
        backgroundColor: '#F5FCFF',
    },
    hello: {
        fontSize: 20,
        textAlign: 'center',
        margin: 10,

    },
});

AppRegistry.registerComponent('MyReactNativeApp', () => HelloWorld);